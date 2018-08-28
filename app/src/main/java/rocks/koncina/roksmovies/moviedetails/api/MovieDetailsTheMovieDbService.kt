package rocks.koncina.roksmovies.moviedetails.api

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface MovieDetailsTheMovieDbService {

    @GET("/3/movie/{movie_id}")
    fun getDetails(@Path("movie_id") movieId: Long,
                   @Query("api_key") key: String)
            : Single<MovieDetails>
}