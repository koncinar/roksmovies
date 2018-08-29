package rocks.koncina.roksmovies.movieslist.view

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import rocks.koncina.roksmovies.MainActivity
import rocks.koncina.roksmovies.R
import rocks.koncina.roksmovies.databinding.FragmentMoviesListBinding
import rocks.koncina.roksmovies.movieslist.viewmodel.MoviesListViewModel
import rocks.koncina.roksmovies.setTitle


private const val ARG_QUERY = "arg_query"

class MoviesListFragment : Fragment() {

    private lateinit var viewModel: MoviesListViewModel
    private var searchQuery: String? = null


    override fun onAttach(context: Context?) {
        super.onAttach(context)

        val activity = (context as MainActivity)

        viewModel = ViewModelProviders.of(this, activity.viewModelFactory).get(MoviesListViewModel::class.java)

        val adapter = MoviesListAdapter(activity::openMovieDetails)
        viewModel.adapter = adapter

        // add listener for changes - whenever the value of LiveData changes, the adapter is updated
        viewModel.movies.observe(context as LifecycleOwner,
                Observer { adapter.movies = it.orEmpty() })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        searchQuery = arguments?.getString(ARG_QUERY)

        viewModel.init(searchQuery)
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
            FragmentMoviesListBinding
                    .inflate(inflater, container, false)
                    .also { it.viewModel = viewModel }
                    .root


    override fun onResume() {
        super.onResume()

        val searchQuery = searchQuery

        if (searchQuery == null || searchQuery.isEmpty()) {
            // main page, showing popular movies
            setTitle(getString(R.string.title_list_popular))

        } else {
            // search page, showing search results
            setTitle(getString(R.string.title_search_movie, searchQuery))
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(query: String? = null) =
                MoviesListFragment().apply {
                    if (query != null) {
                        arguments = Bundle().apply {
                            putString(ARG_QUERY, query)
                        }
                    }
                }
    }
}