package rocks.koncina.roksmovies.movieslist.cache

import android.arch.persistence.room.*
import io.reactivex.Single

@Dao
interface MoviesDao {
    @Query("SELECT * FROM MovieEntity")
    fun getAll(): Single<List<MovieEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(movies: List<MovieEntity>)

    @Query("DELETE FROM MovieEntity")
    fun deleteAll()

    @Transaction
    fun clearAndInsertAll(movies: List<MovieEntity>) {
        deleteAll()
        insertAll(movies)
    }
}