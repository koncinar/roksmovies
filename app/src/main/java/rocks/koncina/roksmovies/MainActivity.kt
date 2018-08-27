package rocks.koncina.roksmovies

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import rocks.koncina.roksmovies.movieslist.factories.InstanceFactory
import rocks.koncina.roksmovies.movieslist.factories.ViewModelFactory

class MainActivity : AppCompatActivity() {

    val viewModelFactory by lazy { ViewModelFactory(InstanceFactory()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
    }
}
