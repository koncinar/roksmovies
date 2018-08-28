package rocks.koncina.roksmovies.movieslist.view

import android.support.v7.widget.RecyclerView
import android.view.View
import rocks.koncina.roksmovies.databinding.ListItemMovieBinding
import rocks.koncina.roksmovies.movieslist.api.Movie

class MovieViewHolder(
        itemView: View,
        private val movieSelectedListener: (Movie) -> Unit
) : RecyclerView.ViewHolder(itemView) {

    private val binding = ListItemMovieBinding.bind(itemView)

    init {
        binding.clickListener = View.OnClickListener { binding.movie?.let(movieSelectedListener::invoke) }
    }

    fun bind(movie: Movie) {
        binding.movie = movie
    }
}
