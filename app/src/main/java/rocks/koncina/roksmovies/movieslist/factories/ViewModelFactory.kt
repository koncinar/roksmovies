package rocks.koncina.roksmovies.movieslist.factories

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import rocks.koncina.roksmovies.movieslist.viewmodel.MoviesListViewModel
import java.security.InvalidParameterException

class ViewModelFactory(private val instanceFactory: InstanceFactory) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        if (modelClass.isAssignableFrom(MoviesListViewModel::class.java)) {
            return MoviesListViewModel(instanceFactory.moviesRepository) as T
        }

        throw InvalidParameterException("Unknown model class $modelClass")
    }
}