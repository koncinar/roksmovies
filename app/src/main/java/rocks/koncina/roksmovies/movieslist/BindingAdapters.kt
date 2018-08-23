package rocks.koncina.roksmovies.movieslist

import android.databinding.BindingAdapter
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView

@BindingAdapter("movies")
fun RecyclerView.setMovies(movies: List<Movie>) {
    layoutManager = LinearLayoutManager(context)

    val moviesListAdapter = MoviesListAdapter()
    moviesListAdapter.movies = movies

    adapter = moviesListAdapter
}

