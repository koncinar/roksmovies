package rocks.koncina.roksmovies.moviedetails.viewmodel

import android.arch.lifecycle.ViewModel
import android.databinding.ObservableField
import android.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import rocks.koncina.roksmovies.moviedetails.model.MovieDetailsRepository
import rocks.koncina.roksmovies.movieslist.api.Movie

class MovieDetailsViewModel(
        private val movieDetailsRepository: MovieDetailsRepository
) : ViewModel() {

    val title = ObservableField<String>()
    val description = ObservableField<String>()
    val language = ObservableField<String>()
    val releaseYear = ObservableField<String>()
    val thumbnailUrl = ObservableField<String>()
    val homePageLink = ObservableField<String>()
    val genresNames = ObservableField<String>()
    val score = ObservableField<String>()
    val runtime = ObservableField<String>()
    val revenue = ObservableField<String>()

    // todo: display error message
    var error: ObservableField<Throwable> = ObservableField()

    private val subscription: Disposable

    lateinit var urlClickListener: UrlClickListener

    init {
        subscription = movieDetailsRepository.behaviorSubject
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            Log.i(MovieDetailsViewModel::class.java.simpleName, "Details fetched $it")
                            title.set(it?.title)
                            description.set(it?.description)
                            language.set(it?.language)
                            releaseYear.set(it?.getReleaseYear())
                            thumbnailUrl.set(it?.thumbnailUrl)
                            homePageLink.set(it?.homePageLink)
                            genresNames.set(it?.getGenresNames())
                            score.set(it?.getScorePrintable())
                            runtime.set(formatRuntime(it?.runtime))
                            revenue.set(formatRevenue(it?.revenue))
                        },
                        {
                            Log.e(MovieDetailsViewModel::class.java.simpleName, "Error fetching details", it)
                            error.set(it)
                        })
    }

    fun initialize(movie: Movie, urlClickListener: UrlClickListener) {
        this.urlClickListener = urlClickListener
        movieDetailsRepository.fetchMovieDetails(movie.id!!)
        movieDetailsRepository.setInitialMovie(movie)
    }

    override fun onCleared() {
        subscription.dispose()
    }

    private fun formatRevenue(revenue: Long?) =
            revenue?.let { "%,d $".format(it) }

    private fun formatRuntime(runtime: Int?) =
            runtime?.let { "%d minutes".format(it) }
}

interface UrlClickListener {
    fun click(url: String)
}