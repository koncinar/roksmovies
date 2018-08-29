package rocks.koncina.roksmovies.movieslist.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.databinding.ObservableBoolean
import android.util.Log
import rocks.koncina.roksmovies.movieslist.api.Movie
import rocks.koncina.roksmovies.movieslist.model.MoviesListRepository
import rocks.koncina.roksmovies.movieslist.view.MoviesListAdapter

class MoviesListViewModel(
        private val moviesListRepository: MoviesListRepository
) : ViewModel() {

    val movies = MutableLiveData<List<Movie>>()
    val isRefreshing = ObservableBoolean(false)

    lateinit var adapter: MoviesListAdapter

    private val disposable = moviesListRepository.movies.subscribe(
            {
                movies.postValue(it)
                isRefreshing.set(false)
            },
            {
                Log.e(MoviesListViewModel::class.java.simpleName, "Error while fetching movies. Previous value will remain shown.", it)
            })

    fun init(searchQuery: String?) {
        moviesListRepository.init(searchQuery)
    }

    override fun onCleared() {
        disposable.dispose()
    }

    /**
     * Starts retrieving the list of movies from the server asynchronously,
     * and when it finishes it updates the movies (live data) field.
     */
    fun refresh() {
        isRefreshing.set(true)
        moviesListRepository.refresh()
    }
}