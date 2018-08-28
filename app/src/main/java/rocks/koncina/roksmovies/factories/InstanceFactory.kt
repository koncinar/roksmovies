package rocks.koncina.roksmovies.factories

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import rocks.koncina.roksmovies.BuildConfig
import rocks.koncina.roksmovies.moviedetails.api.MovieDetailsTheMovieDbService
import rocks.koncina.roksmovies.moviedetails.model.MovieDetailsRepository
import rocks.koncina.roksmovies.movieslist.api.TheMovieDbService
import rocks.koncina.roksmovies.movieslist.model.MoviesRepository

class InstanceFactory {

    // each fragment needs a new instance of repository otherwise it's retaining data from the previous one
    fun moviesRepository() = MoviesRepository(theMovieDbService)

    val movieDetailsRepository by lazy { MovieDetailsRepository(movieDetailsTheMovieDbService) }

    private val theMovieDbService by lazy { retrofit.create(TheMovieDbService::class.java) }

    private val movieDetailsTheMovieDbService by lazy { retrofit.create(MovieDetailsTheMovieDbService::class.java) }


    // region Retrofit

    private val retrofit by lazy {
        Retrofit.Builder()
                .baseUrl(BuildConfig.URL_THE_MOVIE_DB)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()
    }

    private val okHttpClient by lazy {
        OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .build()
    }


    private val httpLoggingInterceptor by lazy {
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.HEADERS)
    }

    // endregion
}