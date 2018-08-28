package rocks.koncina.roksmovies.moviedetails.model

import android.util.Log
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import rocks.koncina.roksmovies.BuildConfig
import rocks.koncina.roksmovies.moviedetails.api.MovieDetails
import rocks.koncina.roksmovies.moviedetails.api.MovieDetailsTheMovieDbService


class MovieDetailsRepository(
        private val service: MovieDetailsTheMovieDbService
) {

    val behaviorSubject: BehaviorSubject<MovieDetails> = BehaviorSubject.create()

    fun fetchMovieDetails(movieId: Long) {
        // fetch movie details
        service.getDetails(movieId, BuildConfig.KEY_THE_MOVIE_DB)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .doOnSuccess {
                    // Gson uses reflection to set field values and therefore the genresNames
                    // are never updated unless we manually call the following method
                    it.updateGenreNames()
                }
                .doOnSuccess { Log.i(MovieDetailsRepository::class.java.simpleName, "Movie details fetched: $it") }
                .subscribe(behaviorSubject::onNext, behaviorSubject::onError)
    }

    fun setInitialMovie(movieDetails: MovieDetails) = behaviorSubject.onNext(movieDetails)
}