package rocks.koncina.roksmovies.movieslist.model

import io.reactivex.Single
import io.reactivex.internal.schedulers.ImmediateThinScheduler
import io.reactivex.observers.TestObserver
import io.reactivex.plugins.RxJavaPlugins
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import rocks.koncina.roksmovies.movieslist.api.Movie
import rocks.koncina.roksmovies.movieslist.cache.MovieEntity
import rocks.koncina.roksmovies.movieslist.cache.MoviesDao

class CacheRepositoryTest {

    private val moviesDao = Mockito.mock(MoviesDao::class.java)
    private val cacheRepository = CacheRepository(moviesDao)

    @Before
    fun init() {
        // override schedulers so that the calls are executed immediately
        RxJavaPlugins.setIoSchedulerHandler { _ -> ImmediateThinScheduler.INSTANCE }
        RxJavaPlugins.setComputationSchedulerHandler { _ -> ImmediateThinScheduler.INSTANCE }
    }

    @Test
    fun testSuccess() {
        // given
        Mockito.`when`(moviesDao.getAll()).thenReturn(Single.just(listOf(
                MovieEntity(0L, "Matrix", null, "Action", 3.5F, "1999"),
                MovieEntity(0L, "Matrix 2", null, "Action", 3.2F, "2000"),
                MovieEntity(0L, "Matrix 3", null, "Action", 3.3F, "2001")
        )))

        val testObserver = TestObserver<List<Movie>>()
        cacheRepository.cachedMovies.subscribe(testObserver)

        // when
        cacheRepository.fetch()

        // then
        testObserver
                .assertNoErrors()
                .assertValueCount(1)
                .assertValue(listOf(
                        Movie(0L, "Matrix", null, null, 3.5F, "1999", "Action"),
                        Movie(0L, "Matrix 3", null, null, 3.3F, "2001", "Action"),
                        Movie(0L, "Matrix 2", null, null, 3.2F, "2000", "Action")
                ))

    }

    @Test
    fun testFailure() {
        // given
        Mockito.`when`(moviesDao.getAll()).thenReturn(Single.error(TestException()))

        val testObserver = TestObserver<List<Movie>>()
        cacheRepository.cachedMovies.subscribe(testObserver)

        // when
        cacheRepository.fetch()

        //then
        testObserver
                .assertValueCount(0)
                .assertError(TestException::class.java)
    }

    @Test
    fun testSave() {
        // when
        cacheRepository.save(listOf(
                Movie(0L, "Matrix", null, null, 3.5F, "1999", "Action"),
                Movie(0L, "Matrix 2", null, null, 3.2F, "2000", "Action"),
                Movie(0L, "Matrix 3", null, null, 3.3F, "2001", "Action")
        ))

        // then
        Mockito.verify(moviesDao, Mockito.times(1)).clearAndInsertAll(listOf(
                MovieEntity(0L, "Matrix", null, "Action", 3.5F, "1999"),
                MovieEntity(0L, "Matrix 2", null, "Action", 3.2F, "2000"),
                MovieEntity(0L, "Matrix 3", null, "Action", 3.3F, "2001")
        ))
    }

    class TestException : Throwable("Expected error")
}