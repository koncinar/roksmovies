package rocks.koncina.roksmovies

import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*
import rocks.koncina.roksmovies.databinding.ActivityMainBinding
import rocks.koncina.roksmovies.factories.InstanceFactory
import rocks.koncina.roksmovies.factories.ViewModelFactory
import rocks.koncina.roksmovies.moviedetails.view.MovieDetailsFragment
import rocks.koncina.roksmovies.movieslist.api.Movie
import rocks.koncina.roksmovies.movieslist.view.MoviesListFragment

class MainActivity : AppCompatActivity() {

    val viewModelFactory by lazy { ViewModelFactory(InstanceFactory()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setSupportActionBar(toolbar)

        binding.viewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(MainViewModel::class.java)
                .also {
                    it.onStartSearch = this::startSearch
                }

        supportFragmentManager.beginTransaction()
                .replace(R.id.fragment, MoviesListFragment.newInstance())
                .commit()
    }

    fun openMovieDetails(movie: Movie) {
        supportFragmentManager.beginTransaction()
                .replace(R.id.fragment, MovieDetailsFragment.newInstance(movie))
                .addToBackStack(null)
                .commit()
    }

    private fun startSearch(query: String) {
        supportFragmentManager.beginTransaction()
                .replace(R.id.fragment, MoviesListFragment.newInstance(query))
                .addToBackStack(null)
                .commit()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            android.R.id.home -> {
                supportFragmentManager.popBackStack()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}

fun Fragment.setTitle(title: String) {
    (activity as AppCompatActivity).supportActionBar?.title = title
}

fun Fragment.showUp(show: Boolean) {
    (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(show)
}