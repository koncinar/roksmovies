package rocks.koncina.roksmovies.movieslist.model

import android.util.Log
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import rocks.koncina.roksmovies.movieslist.api.Movie
import rocks.koncina.roksmovies.movieslist.cache.MovieEntity
import rocks.koncina.roksmovies.movieslist.cache.MoviesDao


class CacheRepository(
        private val moviesDao: MoviesDao
) {

    val cachedMovies = BehaviorSubject.create<List<Movie>>()

    fun fetch() {
        moviesDao
                // fetch the data from the DB
                .getAll()
                .subscribeOn(Schedulers.io())
                .doOnSubscribe { Log.i(TAG, "Started reading movies from the DB") }

                // transform MovieEntity -> Movie
                .observeOn(Schedulers.computation())
                .map { it.map(MovieEntity::toMovie) }

                // sort the movies
                .map { it.sortedByDescending { movie -> movie.popularityScore } }

                // publish
                .doOnSuccess { Log.i(TAG, "Movies read from the DB: $it") }
                .subscribe(cachedMovies::onNext, cachedMovies::onError)
    }

    fun save(movies: List<Movie>) {
        Log.i(TAG, "Saving movies to the DB")
        moviesDao.clearAndInsertAll(movies.map { MovieEntity(it) })

        Log.i(TAG, "Movies saved to the DB")
        cachedMovies.onNext(movies)
    }

    companion object {
        private val TAG = CacheRepository::class.java.simpleName
    }
}