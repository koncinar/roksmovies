package rocks.koncina.roksmovies.moviedetails.view


import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import rocks.koncina.roksmovies.MainActivity
import rocks.koncina.roksmovies.databinding.FragmentMovieDetailsBinding
import rocks.koncina.roksmovies.moviedetails.viewmodel.MovieDetailsViewModel
import rocks.koncina.roksmovies.moviedetails.viewmodel.UrlClickListener
import rocks.koncina.roksmovies.movieslist.api.Movie
import rocks.koncina.roksmovies.setTitle
import rocks.koncina.roksmovies.showUp


private const val ARG_ID = "arg_id"
private const val ARG_TITLE = "arg_title"
private const val ARG_THUMBNAIL_URL = "arg_thumbnailUrl"
private const val ARG_GENRE_IDS = "arg_genreIds"
private const val ARG_POPULARITY_SCORE = "arg_popularityScore "
private const val ARG_RELEASE_DATE = "arg_releaseDate"
private const val ARG_GENRES_NAMES = "arg_genresNames"

class MovieDetailsFragment : Fragment() {

    private lateinit var movie: Movie

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            movie = Movie(
                    it.getLong(ARG_ID),
                    it.getString(ARG_TITLE),
                    it.getString(ARG_THUMBNAIL_URL),
                    it.getLongArray(ARG_GENRE_IDS).asList(),
                    it.getFloat(ARG_POPULARITY_SCORE),
                    it.getString(ARG_RELEASE_DATE),
                    it.getString(ARG_GENRES_NAMES)
            )
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = FragmentMovieDetailsBinding.inflate(inflater, container, false)

        val viewModelFactory = (context as MainActivity).viewModelFactory
        val viewModel = ViewModelProviders.of(this, viewModelFactory).get(MovieDetailsViewModel::class.java)
        viewModel.initialize(movie, object : UrlClickListener {
            override fun click(url: String) {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(url)
                startActivity(intent)
            }
        })
        binding.viewModel = viewModel

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        setTitle(movie.title.orEmpty())
        showUp(true)
    }

    companion object {
        @JvmStatic
        fun newInstance(movie: Movie) =
                MovieDetailsFragment().apply {
                    arguments = Bundle().apply {
                        putLong(ARG_ID, movie.id!!)
                        putString(ARG_TITLE, movie.title)
                        putString(ARG_THUMBNAIL_URL, movie.thumbnailUrl)
                        putLongArray(ARG_GENRE_IDS, movie.genreIds?.toLongArray())
                        putFloat(ARG_POPULARITY_SCORE, movie.popularityScore ?: 0F)
                        putString(ARG_RELEASE_DATE, movie.releaseDate)
                        putString(ARG_GENRES_NAMES, movie.genresNames)
                    }
                }
    }
}
