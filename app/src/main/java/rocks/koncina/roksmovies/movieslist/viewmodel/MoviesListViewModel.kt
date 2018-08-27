package rocks.koncina.roksmovies.movieslist.viewmodel

import android.arch.lifecycle.ViewModel
import android.databinding.ObservableBoolean
import rocks.koncina.roksmovies.movieslist.api.Movie
import rocks.koncina.roksmovies.movieslist.model.MoviesRepository

class MoviesListViewModel(private val moviesRepository: MoviesRepository) : ViewModel() {
    val movies = moviesRepository.movies

    var isRefreshing = ObservableBoolean(false)
    // Observer needs to be saved so that it can be removed when the ViewModel will be destroyed
    private val refreshingObserver: (List<Movie>?) -> Unit = { isRefreshing.set(false) }

    init {
        movies.observeForever(refreshingObserver)
        refresh()
    }

    override fun onCleared() {
        movies.removeObserver(refreshingObserver)
    }

    /**
     * Starts retrieving the list of movies from the server asynchronously,
     * and when it finishes it updates the movies (live data) field.
     */
    fun refresh() {
        isRefreshing.set(true)
        moviesRepository.fetchMovies()
    }
}