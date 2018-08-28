package rocks.koncina.roksmovies.movieslist.view

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import rocks.koncina.roksmovies.R
import rocks.koncina.roksmovies.movieslist.api.Movie

class MoviesListAdapter(
        private val movieSelectedListener: (Movie) -> Unit
) : RecyclerView.Adapter<MovieViewHolder>() {

    var movies = listOf<Movie>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            MovieViewHolder(parent.inflate(R.layout.list_item_movie), movieSelectedListener)

    override fun getItemCount(): Int = movies.size

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) = holder.bind(movies[position])


    private fun ViewGroup.inflate(layoutId: Int): View =
            LayoutInflater.from(context)
                    .inflate(layoutId, this, false)

}
