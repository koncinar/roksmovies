package rocks.koncina.roksmovies.movieslist.view

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import rocks.koncina.roksmovies.MainActivity
import rocks.koncina.roksmovies.databinding.FragmentMoviesListBinding
import rocks.koncina.roksmovies.movieslist.viewmodel.MoviesListViewModel

class MoviesListFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = FragmentMoviesListBinding.inflate(inflater, container, false)

        val viewModelFactory = (context as MainActivity).viewModelFactory
        binding.viewModel = ViewModelProviders.of(this, viewModelFactory).get(MoviesListViewModel::class.java)

        return binding.root
    }
}
