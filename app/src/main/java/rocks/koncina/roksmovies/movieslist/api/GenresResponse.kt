package rocks.koncina.roksmovies.movieslist.api

import com.google.gson.annotations.SerializedName

data class GenresResponse(
        @SerializedName("genres") val genres: List<Genre>? = null
)