package data.repository

import data.local.AlbumsLocalDataSource
import data.remote.ITunesApi
import data.remote.mapper.AlbumMapper.toDomain
import domain.model.*
import domain.repository.AlbumsRepository
import kotlinx.coroutines.flow.*

class AlbumsRepositoryImpl(
    private val remoteApi: ITunesApi,
    private val localDataSource: AlbumsLocalDataSource
) : AlbumsRepository {

    override fun getTopAlbums(): Flow<AppResult<List<Album>>> =
        localDataSource.getAlbums()
            .map { albums ->
                if (albums.isNotEmpty()) {
                    AppResult.Success(albums)
                } else {
                    AppResult.Loading
                }
            }
            .catch { emit(AppResult.Error(AlbumError.CacheError)) }

    override suspend fun refreshAlbums(): AppResult<List<Album>> = runCatching {
        val response = remoteApi.getTopAlbums()
        val albums = response.feed.results.toDomain()

        if (albums.isNotEmpty()) {
            localDataSource.saveAlbums(albums)
            albums
        } else {
            throw IllegalStateException("No albums received")
        }
    }.fold(
        onSuccess = { AppResult.Success(it) },
        onFailure = { AppResult.Error(it.toAlbumError()) }
    )
}

private fun Throwable.toAlbumError(): AlbumError = when (this) {
    is io.ktor.client.network.sockets.SocketTimeoutException -> AlbumError.TimeoutError
    is io.ktor.client.plugins.ClientRequestException -> AlbumError.ApiError(
        code = response.status.value,
        message = response.status.description
    )
    is kotlinx.serialization.SerializationException -> AlbumError.ParseError(
        message ?: "Serialization failed"
    )
    is IllegalStateException -> AlbumError.EmptyResponse
    else -> AlbumError.NetworkError
}