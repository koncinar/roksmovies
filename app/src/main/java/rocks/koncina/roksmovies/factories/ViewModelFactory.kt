package rocks.koncina.roksmovies.factories

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import rocks.koncina.roksmovies.MainViewModel
import rocks.koncina.roksmovies.moviedetails.viewmodel.MovieDetailsViewModel
import rocks.koncina.roksmovies.movieslist.viewmodel.MoviesListViewModel
import java.security.InvalidParameterException

class ViewModelFactory(private val instanceFactory: InstanceFactory) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MoviesListViewModel::class.java)) {
            return MoviesListViewModel(instanceFactory.moviesRepository()) as T
        }

        if (modelClass.isAssignableFrom(MovieDetailsViewModel::class.java)) {
            return MovieDetailsViewModel(instanceFactory.movieDetailsRepository) as T
        }

        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel() as T
        }

        throw InvalidParameterException("Unknown model class $modelClass")
    }
}