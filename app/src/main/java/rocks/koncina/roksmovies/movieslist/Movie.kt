package rocks.koncina.roksmovies.movieslist

import kotlin.math.roundToInt

data class Movie(
        val title: String? = null,
        val thumbnailUrl: String? = null,
        val genres: List<Int> = arrayListOf(),
        val popularityScore: Float? = null,
        val releaseDate: String? = null) {

    fun getScorePrintable() =
            if (popularityScore == null) ""
            else (popularityScore * 10).roundToInt().toString()
}