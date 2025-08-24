package data.repository

import data.local.AlbumsLocalDataSource
import data.remote.AlbumsRemoteDataSource
import data.remote.mapper.AlbumMapper.toDomain
import domain.model.*
import domain.repository.AlbumsRepository
import kotlinx.coroutines.flow.*

class AlbumsRepositoryImpl(
    private val remoteDataSource: AlbumsRemoteDataSource,
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

    override suspend fun refreshAlbums(): AppResult<List<Album>> {
        return when (val response = remoteDataSource.getTopAlbums()) {
            is AppResult.Success -> {
                val albums = response.data.feed.results.toDomain()
                if (albums.isNotEmpty()) {
                    localDataSource.saveAlbums(albums)
                    AppResult.Success(albums)
                } else {
                    AppResult.Error(AlbumError.EmptyResponse)
                }
            }

            is AppResult.Error -> response
            is AppResult.Loading -> AppResult.Error(AlbumError.NetworkError())
        }
    }
}