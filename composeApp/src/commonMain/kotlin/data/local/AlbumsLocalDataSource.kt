package data.local

import domain.model.Album
import kotlinx.coroutines.flow.Flow

interface AlbumsLocalDataSource {
    fun getAlbums(): Flow<List<Album>>
    fun getAlbumById(albumId: String): Flow<Album?>
    suspend fun saveAlbums(albums: List<Album>)
}