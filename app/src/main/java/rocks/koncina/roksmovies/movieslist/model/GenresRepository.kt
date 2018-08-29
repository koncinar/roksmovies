package rocks.koncina.roksmovies.movieslist.model

import android.util.Log
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.SingleSubject
import rocks.koncina.roksmovies.BuildConfig
import rocks.koncina.roksmovies.movieslist.api.Movie
import rocks.koncina.roksmovies.movieslist.api.TheMovieDbService


class GenresRepository(
        theMovieDbService: TheMovieDbService
) {

    val genres = SingleSubject.create<Map<Long, String>>()

    init {
        theMovieDbService
                // fetch the data from the server
                .getGenres(BuildConfig.KEY_THE_MOVIE_DB)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe { Log.i(TAG, "Started fetching genres from the API") }


                // Convert the response into a map {id, name}
                .observeOn(Schedulers.computation())
                .map {
                    it.genres.orEmpty()
                            .associateBy({ it.id!! }, { it.name!! })
                }
                .doOnSuccess { Log.i(TAG, "Fetching genres from the API succeeded") }
                .subscribe(genres::onSuccess, genres::onError)
    }


    companion object {
        fun applyGenresToMovies(movies: List<Movie>, genres: Map<Long, String>): List<Movie> {
            movies.forEach { it.updateGenresNames(genres) }
            return movies
        }

        private val TAG = GenresRepository::class.java.simpleName
    }
}

typealias Join = BiFunction<List<Movie>, Map<Long, String>, List<Movie>>
