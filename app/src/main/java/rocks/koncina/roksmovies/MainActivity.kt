package rocks.koncina.roksmovies

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*
import rocks.koncina.roksmovies.factories.InstanceFactory
import rocks.koncina.roksmovies.factories.ViewModelFactory
import rocks.koncina.roksmovies.moviedetails.view.MovieDetailsFragment
import rocks.koncina.roksmovies.movieslist.api.Movie
import rocks.koncina.roksmovies.movieslist.view.MoviesListFragment


class MainActivity : AppCompatActivity() {

    val viewModelFactory by lazy { ViewModelFactory(InstanceFactory(applicationContext)) }

    internal var searchMenuItem: MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        supportFragmentManager.beginTransaction()
                .replace(R.id.fragment, MoviesListFragment.newInstance())
                .commit()

        handleIntent(intent)
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

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
        if (Intent.ACTION_SEARCH == intent.action) {
            val query = intent.getStringExtra(SearchManager.QUERY)
            startSearch(query)

            //close the searchView
            searchMenuItem?.apply {
                val searchView = actionView as SearchView
                searchView.clearFocus()
                searchView.isIconified = true
                collapseActionView()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.options_menu, menu)

        // Associate searchable configuration with the SearchView
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchMenuItem = menu.findItem(R.id.action_search)
        (searchMenuItem?.actionView as SearchView).apply {
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
        }

        return true
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

fun Fragment.showSearch(show: Boolean) {
    (activity as MainActivity).searchMenuItem?.isVisible = show
}