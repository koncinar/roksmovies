package rocks.koncina.roksmovies.movieslist.api

import com.google.gson.annotations.SerializedName

data class Movie(
        @SerializedName("id") val id: Long? = null,
        @SerializedName("title") val title: String? = null,
        @SerializedName("backdrop_path") val thumbnailUrl: String? = null,
        @SerializedName("genre_ids") val genreIds: List<Long>? = null,
        @SerializedName("popularity") val popularityScore: Float? = null,
        @SerializedName("release_date") val releaseDate: String? = null,

        // locally generated data
        var genres: List<Genre>? = null,
        var genresNames: String = "") {

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

    /**
     * Fills in the names for the genres and stores them in the list (field) genres
     */
    fun updateGenresNames(genresMap: Map<Long, String>) {
        if (genreIds == null) {
            return
        }
        genresNames = genreIds
                .mapNotNull { genresMap[it] }
                .joinToString(separator = ", ")
    }
}