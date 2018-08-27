package rocks.koncina.roksmovies.movieslist.api

import com.google.gson.annotations.SerializedName

data class Genre(
        @SerializedName("id") val id: Long? = 0,
        @SerializedName("name") val name: String? = null
)