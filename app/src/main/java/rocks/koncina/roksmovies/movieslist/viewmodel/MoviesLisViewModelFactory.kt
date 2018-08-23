package rocks.koncina.roksmovies.movieslist.viewmodel

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import rocks.koncina.roksmovies.BuildConfig
import rocks.koncina.roksmovies.movieslist.api.TheMovieDbService
import rocks.koncina.roksmovies.movieslist.model.MoviesRepository
import java.security.InvalidParameterException

class MoviesLisViewModelFactory : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        if (modelClass.isAssignableFrom(MoviesListViewModel::class.java)) {
            return MoviesListViewModel(getMoviesRepository()) as T
        }

        throw InvalidParameterException("Unknown model class $modelClass")
    }


    private fun getMoviesRepository() =
            MoviesRepository(getTheMovieDbService())

    private fun getTheMovieDbService() =
            getRetrofit().create(TheMovieDbService::class.java)

    private fun getRetrofit() =
            Retrofit.Builder()
                    .baseUrl(BuildConfig.URL_THE_MOVIE_DB)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(getOkHttpClient())
                    .build()

    private fun getOkHttpClient() =
            OkHttpClient.Builder()
                    .addInterceptor(getHttpLoggingInterceptor())
                    .build()

    private fun getHttpLoggingInterceptor() =
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.HEADERS)
}