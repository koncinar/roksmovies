package rocks.koncina.roksmovies.movieslist.model

import android.util.Log
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import rocks.koncina.roksmovies.BuildConfig
import rocks.koncina.roksmovies.movieslist.api.Movie
import rocks.koncina.roksmovies.movieslist.api.TheMovieDbService


class PopularMoviesRepository(
        private val theMovieDbService: TheMovieDbService,
        private val genresRepository: GenresRepository,
        private val cacheRepository: CacheRepository
) : MoviesRepository {

    override val movies = BehaviorSubject.create<List<Movie>>()

    init {
        cacheRepository.cachedMovies
                .subscribe(movies::onNext, movies::onError)
        cacheRepository.fetch()
    }

    override fun fetch() {
        theMovieDbService
                // fetch the data from the API
                .getPopularMovies(BuildConfig.KEY_THE_MOVIE_DB)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe { Log.i(TAG, "Started fetching movies from the API") }

                // extract the list from the response
                .observeOn(Schedulers.computation())
                .map { it.results.orEmpty() }
                .zipWith(genresRepository.genres, Join({ movies, genres -> GenresRepository.applyGenresToMovies(movies, genres) }))

                // save the movies into the cache
                .doOnSuccess(cacheRepository::save)
                .subscribe({ _ -> Log.i(TAG, "Fetching movies from the API succeeded") },
                        { t -> Log.e(TAG, "Error while fetching movies from the API", t) })
    }

    companion object {
        private val TAG = PopularMoviesRepository::class.java.simpleName
    }
}