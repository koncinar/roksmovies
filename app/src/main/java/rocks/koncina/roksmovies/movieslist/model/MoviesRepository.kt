package rocks.koncina.roksmovies.movieslist.model

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import rocks.koncina.roksmovies.BuildConfig
import rocks.koncina.roksmovies.movieslist.api.MoviesResponse
import rocks.koncina.roksmovies.movieslist.api.TheMovieDbService


class MoviesRepository(private val theMovieDbService: TheMovieDbService) {

    private val mutableMovies = MutableLiveData<List<Movie>>()

    // The public copy of the field should not be mutable
    val movies: LiveData<List<Movie>> = mutableMovies

    fun fetchMovies() {
        theMovieDbService.getDiscoverMovies(BuildConfig.KEY_THE_MOVIE_DB)
                .enqueue(object : Callback<MoviesResponse?> {
                    override fun onResponse(call: Call<MoviesResponse?>?, response: Response<MoviesResponse?>?) =
                            onMoviesFetched(response?.body()?.results)

                    override fun onFailure(call: Call<MoviesResponse?>?, t: Throwable?) =
                            onError(t)
                })
    }

    private fun onMoviesFetched(movies: List<Movie>?) {
        Log.i(MoviesRepository::class.java.simpleName, "Fetched movies: $movies")
        this.mutableMovies.postValue(movies.orEmpty())
    }

    private fun onError(t: Throwable?) {
        Log.e(MoviesRepository::class.java.simpleName, "Movie fetching failed", t)
        mutableMovies.postValue(listOf())
    }
}