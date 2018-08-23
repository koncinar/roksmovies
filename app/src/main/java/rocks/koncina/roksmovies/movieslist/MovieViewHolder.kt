package rocks.koncina.roksmovies.movieslist

import android.support.v7.widget.RecyclerView
import android.view.View
import rocks.koncina.roksmovies.databinding.ListItemMovieBinding

class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val binding = ListItemMovieBinding.bind(itemView)

    fun bind(movie: Movie) {
        binding.movie = movie
    }
}
