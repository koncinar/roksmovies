package rocks.koncina.roksmovies.movieslist

import android.arch.lifecycle.ViewModel
import android.databinding.ObservableArrayList
import android.databinding.ObservableList

class MoviesListViewModel : ViewModel() {

    val movies: ObservableList<Movie> = ObservableArrayList()

    init {
        // just demo data, delete when the real values are retrieved from the server
        movies.addAll(listOf(
                Movie(title = "Matrix", popularityScore = 1f, releaseDate = "1. 1. 1999"),
                Movie(title = "Avatar", popularityScore = 1f, releaseDate = "1. 1. 2008"),
                Movie(title = "The godfather", popularityScore = 1f, releaseDate = "1. 1. 1984"),
                Movie(title = "Jurassic park", popularityScore = 0.8f, releaseDate = "1. 1. 1992"),
                Movie(title = "Jurassic park 2", popularityScore = 0.5f, releaseDate = "1. 1. 1994"),
                Movie(title = "Jurassic world", popularityScore = 0.7f, releaseDate = "1. 1. 2016"),
                Movie(title = "Jurassic world 2", popularityScore = 0.4f, releaseDate = "1. 1. 2018"),
                Movie(title = "Deathpool", popularityScore = 0.9f, releaseDate = "1. 1. 2017"),
                Movie(title = "Home alone", popularityScore = 0.6f),
                Movie(title = "Terminator", popularityScore = 0.9f, releaseDate = "1. 1. 1984"),
                Movie(title = "Terminator 2", popularityScore = 1f, releaseDate = "1. 1. 1989"),
                Movie(title = "Gladiator", releaseDate = "1. 1. 2001")
        ))
    }
}