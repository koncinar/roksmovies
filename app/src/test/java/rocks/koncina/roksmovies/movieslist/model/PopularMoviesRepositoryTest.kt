package rocks.koncina.roksmovies.movieslist.model

import io.reactivex.Single
import io.reactivex.internal.schedulers.ImmediateThinScheduler
import io.reactivex.plugins.RxJavaPlugins
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import rocks.koncina.roksmovies.movieslist.api.*
import rocks.koncina.roksmovies.movieslist.cache.MoviesDao

class PopularMoviesRepositoryTest {

    private val theMovieDbService = Mockito.mock(TheMovieDbService::class.java)
    private val moviesDao = Mockito.mock(MoviesDao::class.java)

    private val genresRepository = GenresRepository(theMovieDbService)
    private val cacheRepository = CacheRepository(moviesDao)

    private val moviesRepository = PopularMoviesRepository(
            theMovieDbService,
            genresRepository,
            cacheRepository
    )

    @Before
    fun init() {
        // override schedulers so that the calls are executed immediately
        RxJavaPlugins.setIoSchedulerHandler { _ -> ImmediateThinScheduler.INSTANCE }
        RxJavaPlugins.setComputationSchedulerHandler { _ -> ImmediateThinScheduler.INSTANCE }

        Mockito.`when`(theMovieDbService.getGenres(Mockito.anyString())).thenReturn(
                Single.just(GenresResponse(listOf(
                        Genre(0L, "Action"),
                        Genre(1L, "Drama"),
                        Genre(2L, "Comedy")
                )))
        )

        Mockito.`when`(moviesDao.getAll()).thenReturn(
                Single.just(listOf())
        )

        moviesRepository.init()
    }

    @Test
    fun testFetch() {
        // given
        Mockito.`when`(theMovieDbService.getPopularMovies(Mockito.anyString()))
                .thenReturn(
                        Single.just(MoviesResponse(results = listOf(
                                Movie(0L, "Matrix", null, listOf(0L), 3.5F, "1999"),
                                Movie(0L, "Matrix 2", null, listOf(0L, 1L), 3.2F, "2000"),
                                Movie(0L, "Matrix 3", null, listOf(0L, 2L, 1L, 3L), 3.3F, "2001")
                        )))
                )

        // when
        moviesRepository.fetch()

        // then
        Assert.assertEquals(
                listOf(
                        Movie(0L, "Matrix", null, listOf(0L), 3.5F, "1999", "Action"),
                        Movie(0L, "Matrix 2", null, listOf(0L, 1L), 3.2F, "2000", "Action, Drama"),
                        Movie(0L, "Matrix 3", null, listOf(0L, 2L, 1L, 3L), 3.3F, "2001", "Action, Comedy, Drama")
                ),
                moviesRepository.movies.value)
    }

    @Test
    fun testFetchEmpty() {
        // given
        Mockito.`when`(theMovieDbService.getPopularMovies(Mockito.anyString()))
                .thenReturn(
                        Single.just(MoviesResponse())
                )

        // when
        moviesRepository.fetch()

        // then
        Assert.assertEquals(
                listOf<Movie>(),
                moviesRepository.movies.value)
    }
}