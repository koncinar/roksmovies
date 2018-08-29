package rocks.koncina.roksmovies.movieslist.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.databinding.ObservableBoolean
import android.util.Log
import rocks.koncina.roksmovies.movieslist.api.Movie
import rocks.koncina.roksmovies.movieslist.model.MoviesRepository
import rocks.koncina.roksmovies.movieslist.view.MoviesListAdapter

class MoviesListViewModel(
        private val moviesRepository: MoviesRepository
) : ViewModel() {

    val movies = MutableLiveData<List<Movie>>()
    val isRefreshing = ObservableBoolean(false)

    lateinit var adapter: MoviesListAdapter
    lateinit var searchQuery: String

    // Observer needs to be saved so that it can be removed when the ViewModel will be destroyed
    private val refreshingObserver: (List<Movie>?) -> Unit = { isRefreshing.set(false) }

    init {
        movies.observeForever(refreshingObserver)
        moviesRepository.movies.subscribe(movies::postValue,
                { Log.e(MoviesListViewModel::class.java.simpleName, "Error while fetching movies. Previous value will remain shown.", it) })
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

        if (searchQuery.isEmpty()) {
            moviesRepository.fetchMovies()

        } else {
            moviesRepository.search(searchQuery)
        }
    }
}