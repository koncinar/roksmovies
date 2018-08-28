package rocks.koncina.roksmovies

import android.databinding.BindingAdapter
import android.widget.ImageView
import com.bumptech.glide.Glide

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