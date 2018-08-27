package rocks.koncina.roksmovies

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import android.databinding.BindingAdapter
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import rocks.koncina.roksmovies.movieslist.api.Movie
import rocks.koncina.roksmovies.movieslist.view.MoviesListAdapter

@BindingAdapter("movies")
fun RecyclerView.setMovies(moviesLiveData: LiveData<List<Movie>>) {
    layoutManager = LinearLayoutManager(context)

    val moviesListAdapter = MoviesListAdapter()
    adapter = moviesListAdapter

    // add listener for changes - whenever the value of LiveData changes, the adapter is updated
    moviesLiveData.observe(context as LifecycleOwner,
            Observer { moviesListAdapter.movies = it.orEmpty() })

}

