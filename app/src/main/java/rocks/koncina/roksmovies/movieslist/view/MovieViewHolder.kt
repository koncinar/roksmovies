package rocks.koncina.roksmovies.movieslist.view

import android.support.v7.widget.RecyclerView
import android.view.View
import rocks.koncina.roksmovies.databinding.ListItemMovieBinding
import rocks.koncina.roksmovies.movieslist.api.Movie

class MovieViewHolder(itemView: View, movieSelectedListener: MovieSelectedListener) : RecyclerView.ViewHolder(itemView) {
    private val binding = ListItemMovieBinding.bind(itemView)

    init {
        binding.movieSelectedListener = movieSelectedListener
    }

    fun bind(movie: Movie) {
        binding.movie = movie
    }
}
