package rocks.koncina.roksmovies.movieslist.model

import com.google.gson.annotations.SerializedName

data class Movie(
        @SerializedName("title") val title: String? = null,
        @SerializedName("backdrop_path") val thumbnailUrl: String? = null,
        @SerializedName("genre_ids") val genres: List<Int> = arrayListOf(),
        @SerializedName("popularity") val popularityScore: Float? = null,
        @SerializedName("release_date") val releaseDate: String? = null) {

    /**
     * Prints the score up to 2 decimals accuracy
     */
    fun getScorePrintable() =
            if (popularityScore == null) ""
            else String.format("%.2f", popularityScore)

    /**
     * Returns the year (first 4 numbers) of the release date
     */
    fun getReleaseYear() =
            if (releaseDate != null && releaseDate.length >= 4) releaseDate.substring(0, 4)
            else ""
}