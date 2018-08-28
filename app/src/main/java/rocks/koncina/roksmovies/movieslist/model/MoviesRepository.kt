package rocks.koncina.roksmovies.movieslist.model

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.util.Log
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import rocks.koncina.roksmovies.BuildConfig
import rocks.koncina.roksmovies.movieslist.api.Movie
import rocks.koncina.roksmovies.movieslist.api.TheMovieDbService


class MoviesRepository(private val theMovieDbService: TheMovieDbService) {

    private val mutableMovies = MutableLiveData<List<Movie>>()

    // The public copy of the field should not be mutable
    val movies: LiveData<List<Movie>> = mutableMovies

    fun fetchMovies() {
        // fetch genres and movies simultaneously
        Single.zip(
                fetchGenresFromApi(),
                fetchMoviesFromApi(),
                Join { genres, movies -> Pair(genres, movies) })

                .observeOn(Schedulers.computation())
                .map { it -> applyGenresToMovies(it.first, it.second) }
                .subscribe(::onMoviesFetched, ::onError)
    }

    private fun onMoviesFetched(movies: List<Movie>) {
        Log.i(MoviesRepository::class.java.simpleName, "Fetched movies: ${movies}")
        mutableMovies.postValue(movies)
    }

    private fun onError(t: Throwable) {
        Log.e(MoviesRepository::class.java.simpleName, "Movie fetching failed", t)
        mutableMovies.postValue(listOf())
    }

    private fun fetchMoviesFromApi(): Single<List<Movie>> = theMovieDbService
            // fetch the data from the server
            .getPopularMovies(BuildConfig.KEY_THE_MOVIE_DB)
            .subscribeOn(Schedulers.io())
            .doOnSuccess { Log.i(MoviesRepository::class.java.simpleName, "Fetching movies from the API succeeded") }

            // extract the list from the response
            .observeOn(Schedulers.computation())
            .map { it.results.orEmpty() }

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
                fetchGenresFromApi(),
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
