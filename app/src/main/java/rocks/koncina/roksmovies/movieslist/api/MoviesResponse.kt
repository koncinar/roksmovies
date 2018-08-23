package rocks.koncina.roksmovies.movieslist.api

import com.google.gson.annotations.SerializedName
import rocks.koncina.roksmovies.movieslist.model.Movie

data class MoviesResponse(
        @SerializedName("page") val page: Int? = null,
        @SerializedName("total_results") val totalResult: Int? = null,
        @SerializedName("total_pages") val totalPages: Int? = null,
        @SerializedName("results") val results: List<Movie>? = null
)