package rocks.koncina.roksmovies.movieslist.api

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query


interface TheMovieDbService {

    @GET("/3/discover/movie")
    fun getDiscoverMovies(@Query("api_key") key: String): Single<MoviesResponse>

    @GET("/3/genre/movie/list")
    fun getGenres(@Query("api_key") key: String): Single<GenresResponse>
}