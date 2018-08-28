package rocks.koncina.roksmovies.moviedetails.api

import com.google.gson.annotations.SerializedName
import rocks.koncina.roksmovies.movieslist.api.Genre

data class MovieDetails(
        @SerializedName("title") val title: String? = null,
        @SerializedName("backdrop_path") val thumbnailUrl: String? = null,
        @SerializedName("genres") val genres: List<Genre>? = null,
        @SerializedName("popularity") val popularityScore: Float? = null,
        @SerializedName("release_date") val releaseDate: String? = null,

        @SerializedName("overview") val description: String? = null,
        @SerializedName("runtime") val runtime: Int? = null,
        @SerializedName("revenue") val revenue: Long? = null,
        @SerializedName("original_language") val language: String? = null,
        @SerializedName("homepage") val homePageLink: String? = null) {

    /**
     * Prints the popularity score with 2 decimals
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

    /**
     * Returns a single String that combines all genre names, separated by ", ".
     * For example "Drama, Action, Comedy".
     */
    fun getGenresNames() = genres
            .orEmpty()
            .map { it.name }
            .joinToString(separator = ", ")
}