package rocks.koncina.roksmovies.movieslist.model

import io.reactivex.subjects.BehaviorSubject
import rocks.koncina.roksmovies.movieslist.api.Movie


class MoviesListRepository(
        private val popularMoviesRepository: PopularMoviesRepository,
        private val searchMoviesRepository: SearchMoviesRepository
) {

    val movies = BehaviorSubject.create<List<Movie>>()

    private lateinit var moviesRepository: MoviesRepository

    fun init(searchQuery: String?) {

        moviesRepository =
                if (searchQuery == null) popularMoviesRepository
                else searchMoviesRepository.init(searchQuery)

        moviesRepository.movies
                .subscribe(movies::onNext, movies::onError)

        refresh()
    }

    fun refresh() {
        moviesRepository.fetch()
    }
}