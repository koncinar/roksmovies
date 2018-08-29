package rocks.koncina.roksmovies.movieslist.model

import android.util.Log
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import rocks.koncina.roksmovies.BuildConfig
import rocks.koncina.roksmovies.movieslist.api.Movie
import rocks.koncina.roksmovies.movieslist.api.TheMovieDbService


class SearchMoviesRepository(
        private val theMovieDbService: TheMovieDbService,
        private val genresRepository: GenresRepository
) : MoviesRepository {

    override val movies = BehaviorSubject.create<List<Movie>>()
    private lateinit var searchQuery: String

    fun init(searchQuery: String): SearchMoviesRepository {
        this.searchQuery = searchQuery
        return this
    }

    override fun fetch() {
        theMovieDbService
                // fetch the data from the API
                .search(searchQuery, BuildConfig.KEY_THE_MOVIE_DB)
                .subscribeOn(Schedulers.io())
                .doOnSuccess { Log.i(TAG, "Searching for movies with title \"$searchQuery\" from the API succeeded") }

                // extract the list from the response
                .observeOn(Schedulers.computation())
                .map { it.results.orEmpty() }
                .zipWith(genresRepository.genres, Join { movies, genres -> GenresRepository.applyGenresToMovies(movies, genres) })

                // sort the movies
                .map { it.sortedByDescending { movie -> movie.popularityScore } }
                .subscribe(movies::onNext, movies::onError)

    }

    companion object {
        private val TAG = SearchMoviesRepository::class.java.simpleName
    }
}