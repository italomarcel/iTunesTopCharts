package domain.repository

import domain.model.Album
import domain.model.AppResult
import kotlinx.coroutines.flow.Flow

interface AlbumsRepository {
    fun getTopAlbums(): Flow<AppResult<List<Album>>>
    suspend fun refreshAlbums(): AppResult<List<Album>>
}