package rocks.koncina.roksmovies.moviedetails.model

import android.util.Log
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import rocks.koncina.roksmovies.BuildConfig
import rocks.koncina.roksmovies.moviedetails.api.MovieDetails
import rocks.koncina.roksmovies.moviedetails.api.MovieDetailsTheMovieDbService
import rocks.koncina.roksmovies.movieslist.api.Movie


class MovieDetailsRepository(private val movieDetailsTheMovieDbService: MovieDetailsTheMovieDbService) {

    val behaviorSubject: BehaviorSubject<MovieDetails> = BehaviorSubject.create()

    fun fetchMovieDetails(movieId: Long) {
        // fetch movie details
        movieDetailsTheMovieDbService.getDetails(movieId, BuildConfig.KEY_THE_MOVIE_DB)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .doOnSuccess { Log.i(MovieDetailsRepository::class.java.simpleName, "Movie details fetched: $it") }
                .subscribe(behaviorSubject::onNext, behaviorSubject::onError)
    }

    fun setInitialMovie(movie: Movie) =
            behaviorSubject.onNext(MovieDetails(
                    title = movie.title,
                    thumbnailUrl = movie.thumbnailUrl,
                    popularityScore = movie.popularityScore,
                    releaseDate = movie.releaseDate,
                    genres = movie.genres))
}