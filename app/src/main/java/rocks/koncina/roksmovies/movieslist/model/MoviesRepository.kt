package rocks.koncina.roksmovies.movieslist.model

import io.reactivex.subjects.BehaviorSubject
import rocks.koncina.roksmovies.movieslist.api.Movie

interface MoviesRepository {
    val movies: BehaviorSubject<List<Movie>>
    fun init()
    fun fetch()
}