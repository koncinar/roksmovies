package rocks.koncina.roksmovies

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import android.databinding.BindingAdapter
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.ImageView
import com.bumptech.glide.Glide
import rocks.koncina.roksmovies.movieslist.api.Movie
import rocks.koncina.roksmovies.movieslist.view.MovieSelectedListener
import rocks.koncina.roksmovies.movieslist.view.MoviesListAdapter

@BindingAdapter("movies", "movieSelectedListener")
fun RecyclerView.setMovies(moviesLiveData: LiveData<List<Movie>>, movieSelectedListener: MovieSelectedListener) {
    layoutManager = LinearLayoutManager(context)

    val moviesListAdapter = MoviesListAdapter(movieSelectedListener)
    adapter = moviesListAdapter

    // add listener for changes - whenever the value of LiveData changes, the adapter is updated
    moviesLiveData.observe(context as LifecycleOwner,
            Observer { moviesListAdapter.movies = it.orEmpty() })

}

@BindingAdapter("thumbnail")
fun ImageView.setThumbnail(url: String?) {
    if (url == null) {
        setImageDrawable(null)

    } else {
        Glide.with(context)
                .load(BuildConfig.URL_THE_MOVIE_DB_THUMBNAILS + url)
                .into(this)
    }
}

