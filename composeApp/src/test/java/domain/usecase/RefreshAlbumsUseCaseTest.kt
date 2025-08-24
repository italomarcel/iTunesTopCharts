package domain.usecase

import domain.model.Album
import domain.model.AppResult
import domain.model.AlbumError
import domain.repository.AlbumsRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Test
import java.util.concurrent.atomic.AtomicInteger

class RefreshAlbumsUseCaseTest {

    private val testAlbum = Album(
        id = "1",
        name = "Test Album",
        artistName = "Test Artist",
        artworkUrl = "http://example.com/art.jpg",
        releaseDate = "2024-01-01",
        category = "Pop",
        albumUrl = "http://example.com/album"
    )

    private fun repoWithRefresh(refreshImpl: suspend () -> AppResult<List<Album>>): AlbumsRepository =
        object : AlbumsRepository {
            override suspend fun refreshAlbums(): AppResult<List<Album>> = refreshImpl()
            override fun getTopAlbums(): Flow<AppResult<List<Album>>> = emptyFlow()
        }

    @Test
    fun `Successful refresh returns list of albums`() = runTest {
        val repo = repoWithRefresh { AppResult.Success(listOf(testAlbum)) }
        val useCase = RefreshAlbumsUseCase(repo)
        val result = useCase()
        assertEquals(AppResult.Success(listOf(testAlbum)), result)
    }

    @Test
    fun `Successful refresh returns empty list`() = runTest {
        val repo = repoWithRefresh { AppResult.Success(emptyList()) }
        val useCase = RefreshAlbumsUseCase(repo)
        val result = useCase()
        assertEquals(AppResult.Success(emptyList<Album>()), result)
    }

    @Test
    fun `Repository returns empty response error`() = runTest {
        val repo = repoWithRefresh { AppResult.Error(AlbumError.EmptyResponse) }
        val useCase = RefreshAlbumsUseCase(repo)
        val result = useCase()
        assertTrue(result is AppResult.Error)
        assertEquals(AlbumError.EmptyResponse, (result as AppResult.Error).error)
    }

    @Test
    fun `Repository returns network error`() = runTest {
        val repo = repoWithRefresh { AppResult.Error(AlbumError.NetworkError("Network error")) }
        val useCase = RefreshAlbumsUseCase(repo)
        val result = useCase()
        assertTrue(result is AppResult.Error)
        assertTrue((result as AppResult.Error).error is AlbumError.NetworkError)
    }

    @Test
    fun `Repository returns cache error`() = runTest {
        val repo = repoWithRefresh { AppResult.Error(AlbumError.CacheError) }
        val useCase = RefreshAlbumsUseCase(repo)
        val result = useCase()
        assertTrue(result is AppResult.Error)
        assertEquals(AlbumError.CacheError, (result as AppResult.Error).error)
    }

    @Test
    fun `Coroutine cancellation during repository call`() = runTest {
        val started = CompletableDeferred<Unit>()
        val repo = repoWithRefresh {
            started.complete(Unit)
            delay(Long.MAX_VALUE)
            AppResult.Success(emptyList())
        }
        val useCase = RefreshAlbumsUseCase(repo)
        val job = launch { useCase() }
        started.await()
        job.cancelAndJoin()
        assertTrue(job.isCancelled)
    }

    @Test
    fun `Concurrent calls to invoke`() = runTest {
        val counter = AtomicInteger(0)
        val repo = repoWithRefresh {
            delay(10)
            counter.incrementAndGet()
            AppResult.Success(listOf(testAlbum))
        }
        val useCase = RefreshAlbumsUseCase(repo)
        val results = coroutineScope {
            List(10) { async { useCase() } }.awaitAll()
        }
        assertEquals(10, counter.get())
        results.forEach { assertEquals(AppResult.Success(listOf(testAlbum)), it) }
    }
}