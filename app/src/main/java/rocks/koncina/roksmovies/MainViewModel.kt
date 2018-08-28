package rocks.koncina.roksmovies

import android.arch.lifecycle.ViewModel
import android.databinding.ObservableBoolean
import android.support.v7.widget.SearchView

class MainViewModel : ViewModel() {
    val searching = ObservableBoolean()
    var onStartSearch: ((query: String) -> Unit)? = null

    val searchListener = object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            // start the movie search and hide the search bar
            query?.let { onStartSearch?.invoke(it) }
            searching.set(false)

            return true
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            // if the clear button is clicked hide the search bar
            // todo: implement better behavior for dismissing the search bar
            if (newText.isNullOrEmpty()) {
                searching.set(false)
            }
            return false
        }
    }

    fun showSearchingInterface() {
        searching.set(true)
    }
}