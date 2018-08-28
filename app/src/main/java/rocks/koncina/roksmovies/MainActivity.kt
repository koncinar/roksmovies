package rocks.koncina.roksmovies

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import rocks.koncina.roksmovies.factories.InstanceFactory
import rocks.koncina.roksmovies.factories.ViewModelFactory
import rocks.koncina.roksmovies.moviedetails.view.MovieDetailsFragment
import rocks.koncina.roksmovies.movieslist.api.Movie
import rocks.koncina.roksmovies.movieslist.view.MoviesListFragment

class MainActivity : AppCompatActivity() {

    val viewModelFactory by lazy { ViewModelFactory(InstanceFactory()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        supportFragmentManager.beginTransaction()
                .replace(R.id.fragment, MoviesListFragment.newInstance())
                .commit()
    }

    fun openMovieDetails(movie: Movie) {
        supportFragmentManager.beginTransaction()
                .replace(R.id.fragment, MovieDetailsFragment.newInstance(movie))
                .addToBackStack(MovieDetailsFragment::class.java.simpleName)
                .commit()
    }
}
