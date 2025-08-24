package domain.usecase

import domain.model.Album
import domain.model.AppResult
import domain.model.AlbumError
import domain.repository.AlbumsRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Test
import java.util.concurrent.atomic.AtomicInteger

class RefreshAlbumsUseCaseTest {

    private companion object {
        const val CONCURRENT_CALLS_COUNT = 10
        const val DELAY_MS = 10L

        val TEST_ALBUM = Album(
            id = "1",
            name = "Test Album",
            artistName = "Test Artist",
            artworkUrl = "http://example.com/art.jpg",
            releaseDate = "2024-01-01",
            category = "Pop",
            albumUrl = "http://example.com/album"
        )

        val TEST_ALBUMS = listOf(TEST_ALBUM)
        val EMPTY_ALBUMS = emptyList<Album>()
    }

    private fun mockRepository(
        refreshResult: suspend () -> AppResult<List<Album>>
    ): AlbumsRepository = object : AlbumsRepository {
        override suspend fun refreshAlbums(): AppResult<List<Album>> = refreshResult()
        override fun getTopAlbums(): Flow<AppResult<List<Album>>> = emptyFlow()
    }

    @Test
    fun `given repository returns albums when refresh invoked then returns success with albums`() =
        runTest {
            // Given
            val repository = mockRepository { AppResult.Success(TEST_ALBUMS) }
            val useCase = RefreshAlbumsUseCase(repository)

            // When
            val results = useCase().toList()

            // Then
            assertEquals(2, results.size)
            assertEquals(AppResult.Loading, results[0])
            assertEquals(AppResult.Success(TEST_ALBUMS), results[1])
        }

    @Test
    fun `given repository returns empty list when refresh invoked then returns success with empty list`() =
        runTest {
            // Given
            val repository = mockRepository { AppResult.Success(EMPTY_ALBUMS) }
            val useCase = RefreshAlbumsUseCase(repository)

            // When
            val results = useCase().toList()

            // Then
            assertEquals(2, results.size)
            assertEquals(AppResult.Loading, results[0])
            assertEquals(AppResult.Success(EMPTY_ALBUMS), results[1])
        }

    @Test
    fun `given repository returns empty response error when refresh invoked then returns error`() =
        runTest {
            // Given
            val expectedError = AlbumError.EmptyResponse
            val repository = mockRepository { AppResult.Error(expectedError) }
            val useCase = RefreshAlbumsUseCase(repository)

            // When
            val results = useCase().toList()

            // Then
            assertEquals(2, results.size)
            assertEquals(AppResult.Loading, results[0])
            assertErrorResult(results[1], expectedError)
        }

    @Test
    fun `given repository returns network error when refresh invoked then returns network error`() =
        runTest {
            // Given
            val networkError = AlbumError.NetworkError("Connection failed")
            val repository = mockRepository { AppResult.Error(networkError) }
            val useCase = RefreshAlbumsUseCase(repository)

            // When
            val results = useCase().toList()

            // Then
            assertEquals(2, results.size)
            assertEquals(AppResult.Loading, results[0])
            assertErrorResult(results[1], networkError)
            assertTrue(
                "Error should be NetworkError",
                (results[1] as AppResult.Error).error is AlbumError.NetworkError
            )
        }

    @Test
    fun `given repository returns cache error when refresh invoked then returns cache error`() =
        runTest {
            // Given
            val cacheError = AlbumError.CacheError
            val repository = mockRepository { AppResult.Error(cacheError) }
            val useCase = RefreshAlbumsUseCase(repository)

            // When
            val results = useCase().toList()

            // Then
            assertEquals(2, results.size)
            assertEquals(AppResult.Loading, results[0])
            assertErrorResult(results[1], cacheError)
        }

    @Test
    fun `given repository returns timeout error when refresh invoked then returns timeout error`() =
        runTest {
            // Given
            val timeoutError = AlbumError.TimeoutError
            val repository = mockRepository { AppResult.Error(timeoutError) }
            val useCase = RefreshAlbumsUseCase(repository)

            // When
            val results = useCase().toList()

            // Then
            assertEquals(2, results.size)
            assertEquals(AppResult.Loading, results[0])
            assertErrorResult(results[1], timeoutError)
        }

    @Test
    fun `given long running repository call when cancelled then coroutine is cancelled properly`() =
        runTest {
            // Given
            val started = CompletableDeferred<Unit>()
            val repository = mockRepository {
                started.complete(Unit)
                delay(Long.MAX_VALUE) // Infinite delay to test cancellation
                AppResult.Success(EMPTY_ALBUMS)
            }
            val useCase = RefreshAlbumsUseCase(repository)

            // When
            val job = launch { useCase().toList() }
            started.await() // Ensure the call has started
            job.cancelAndJoin()

            // Then
            assertTrue("Job should be cancelled", job.isCancelled)
        }

    @Test
    fun `given repository call takes time when multiple concurrent calls made then all succeed independently`() =
        runTest {
            // Given
            val callCounter = AtomicInteger(0)
            val repository = mockRepository {
                delay(DELAY_MS)
                callCounter.incrementAndGet()
                AppResult.Success(TEST_ALBUMS)
            }
            val useCase = RefreshAlbumsUseCase(repository)

            // When
            val results = coroutineScope {
                List(CONCURRENT_CALLS_COUNT) {
                    async { useCase().toList() }
                }.awaitAll()
            }

            // Then
            assertEquals("All calls should complete", CONCURRENT_CALLS_COUNT, callCounter.get())
            results.forEach { flowResults ->
                assertEquals("Each flow should emit 2 values", 2, flowResults.size)
                assertEquals("First should be Loading", AppResult.Loading, flowResults[0])
                assertEquals(
                    "Second should be Success", AppResult.Success(TEST_ALBUMS), flowResults[1]
                )
            }
        }

    private fun assertErrorResult(result: AppResult<List<Album>>, expectedError: AlbumError) {
        assertTrue("Result should be error", result is AppResult.Error)
        assertEquals(
            "Error should match expected", expectedError, (result as AppResult.Error).error
        )
    }
}