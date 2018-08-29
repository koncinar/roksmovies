package rocks.koncina.roksmovies.movieslist.cache

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import rocks.koncina.roksmovies.movieslist.api.Movie

@Entity
data class MovieEntity(
        @PrimaryKey var id: Long?,
        @ColumnInfo(name = "title") var title: String?,
        @ColumnInfo(name = "backdrop_path") var thumbnailUrl: String?,
        @ColumnInfo(name = "genres") var genres: String?,
        @ColumnInfo(name = "popularity") var popularityScore: Float?,
        @ColumnInfo(name = "release_date") var releaseDate: String?
) {

    constructor(movie: Movie) : this(movie.id, movie.title, movie.thumbnailUrl, movie.genresNames, movie.popularityScore, movie.releaseDate)

    fun toMovie() = Movie(id, title, thumbnailUrl, null, popularityScore, releaseDate, genres.orEmpty())
}