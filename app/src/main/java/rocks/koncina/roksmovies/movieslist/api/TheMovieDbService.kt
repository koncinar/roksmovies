package rocks.koncina.roksmovies.movieslist.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface TheMovieDbService {

    @GET("/3/discover/movie")
    fun getDiscoverMovies(@Query("api_key") key: String): Call<MoviesResponse>

}