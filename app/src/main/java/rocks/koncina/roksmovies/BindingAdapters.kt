package rocks.koncina.roksmovies

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import android.databinding.BindingAdapter
import android.graphics.drawable.Drawable
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.DrawableImageViewTarget
import com.bumptech.glide.request.transition.Transition
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

@BindingAdapter("thumbnail")
fun ImageView.setThumbnail(url: String?) {
    if (url == null) {
        setImageDrawable(null)
        visibility = View.GONE

    } else {
        Glide.with(context)
                .load(BuildConfig.URL_THE_MOVIE_DB_THUMBNAILS + url)
                .into(object : DrawableImageViewTarget(this) {
                    override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                        visibility = View.VISIBLE
                        super.onResourceReady(resource, transition)
                    }

                    override fun onLoadFailed(errorDrawable: Drawable?) {
                        visibility = View.GONE
                        super.onLoadFailed(errorDrawable)
                    }
                })
    }
}

