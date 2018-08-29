package rocks.koncina.roksmovies.movieslist.model

import android.util.Log
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import rocks.koncina.roksmovies.BuildConfig
import rocks.koncina.roksmovies.movieslist.api.Movie
import rocks.koncina.roksmovies.movieslist.api.TheMovieDbService
import rocks.koncina.roksmovies.movieslist.cache.MovieEntity
import rocks.koncina.roksmovies.movieslist.cache.MoviesDao
import java.util.concurrent.TimeUnit


class MoviesRepository(
        private val theMovieDbService: TheMovieDbService,
        private val moviesDao: MoviesDao
) {

    val movies = BehaviorSubject.create<List<Movie>>()

    fun fetchMovies() {
        // fetch genres and movies simultaneously
        Observable.combineLatest(
                fetchGenresFromApi(),
                fetchMoviesFromAllSources(),
                Join { genres, movies -> Pair(genres, movies) })

                .observeOn(Schedulers.computation())
                .map { it -> applyGenresToMovies(it.first, it.second) }
                .subscribe(::onMoviesFetched, ::onError)
    }

    private fun onMoviesFetched(movies: List<Movie>) {
        Log.i(MoviesRepository::class.java.simpleName, "Fetched movies: ${movies}")
        this.movies.onNext(movies)
    }

    private fun onError(t: Throwable) {
        Log.e(MoviesRepository::class.java.simpleName, "Movie fetching failed", t)
        movies.onError(t)
    }

    private fun fetchMoviesFromAllSources(): Observable<List<Movie>> = Observable.merge(
            fetchMoviesFromDb(),
            fetchMoviesFromApi()
    ).map { it.sortedByDescending { movie -> movie.popularityScore } }

    private fun fetchMoviesFromDb(): Observable<List<Movie>> = moviesDao
            .getAll()
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.computation())
            .doOnSuccess { Log.i(MoviesRepository::class.java.simpleName, "MovieEntities read from the DB: $it") }
            .map {
                Log.i(MoviesRepository::class.java.simpleName, "Converting the list: $it")
                val map = it.map(MovieEntity::toMovie)
                Log.i(MoviesRepository::class.java.simpleName, "into: $map")
                map
            }
            .doOnSuccess { Log.i(MoviesRepository::class.java.simpleName, "Movies read from the DB: $it") }
            .toObservable()

    private fun fetchMoviesFromApi(): Observable<List<Movie>> = theMovieDbService
            // fetch the data from the server
            .getPopularMovies(BuildConfig.KEY_THE_MOVIE_DB)
            .subscribeOn(Schedulers.io())
            .doOnSuccess { Log.i(MoviesRepository::class.java.simpleName, "Fetching movies from the API succeeded") }

            // extract the list from the response
            .observeOn(Schedulers.computation())
            .map { it.results.orEmpty() }
            .doOnSuccess { saveMovies(it) }
            .delay(10, TimeUnit.SECONDS)
            .toObservable()

    private fun saveMovies(movies: List<Movie>) {
        moviesDao.clearAndInsertAll(movies.map { MovieEntity(it) })
    }

    private fun fetchGenresFromApi() = theMovieDbService
            // fetch the data from the server
            .getGenres(BuildConfig.KEY_THE_MOVIE_DB)
            .subscribeOn(Schedulers.io())
            .doOnSuccess { Log.i(MoviesRepository::class.java.simpleName, "Fetching genres from the API succeeded") }

            // Convert the response into a map {id, name}
            .observeOn(Schedulers.computation())
            .map {
                it.genres.orEmpty()
                        .associateBy({ it.id!! }, { it.name!! })
            }
            .toObservable()
            // cache the result because it should only be called once
            .cache()


    private fun applyGenresToMovies(genres: Map<Long, String>, movies: List<Movie>): List<Movie> {
        movies.forEach {
            it.updateGenresNames(genres)
        }

        return movies
    }

    fun search(movieTitle: String) {
        Single.zip(
                fetchGenresFromApi().lastOrError(),
                fetchSearchMoviesFromApi(movieTitle),
                Join { genres, movies -> Pair(genres, movies) })

                .observeOn(Schedulers.computation())
                .map { it -> applyGenresToMovies(it.first, it.second) }
                .subscribe(::onMoviesFetched, ::onError)
    }

    private fun fetchSearchMoviesFromApi(movieTitle: String) = theMovieDbService
            .search(movieTitle, BuildConfig.KEY_THE_MOVIE_DB)
            .subscribeOn(Schedulers.io())
            .doOnSuccess { Log.i(MoviesRepository::class.java.simpleName, "Searching for movies with title \"$movieTitle\" from the API succeeded") }

            .observeOn(Schedulers.computation())
            .map { it.results.orEmpty() }
}

private typealias Join = BiFunction<Map<Long, String>, List<Movie>, Pair<Map<Long, String>, List<Movie>>>
