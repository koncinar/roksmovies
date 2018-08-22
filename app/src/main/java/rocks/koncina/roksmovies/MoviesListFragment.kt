package rocks.koncina.roksmovies

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import rocks.koncina.roksmovies.databinding.FragmentMoviesListBinding

class MoviesListFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = FragmentMoviesListBinding.inflate(inflater, container, false)

        binding.viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        return binding.root
    }
}
