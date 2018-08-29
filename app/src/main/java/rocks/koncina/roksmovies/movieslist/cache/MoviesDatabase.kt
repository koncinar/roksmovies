package rocks.koncina.roksmovies.movieslist.cache

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase

@Database(entities = [MovieEntity::class], version = 1)
abstract class MoviesDatabase : RoomDatabase() {
    abstract fun movieDao(): MoviesDao
}