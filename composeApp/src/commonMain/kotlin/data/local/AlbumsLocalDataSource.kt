package data.local

import domain.model.Album
import domain.model.AlbumId
import kotlinx.coroutines.flow.Flow

interface AlbumsLocalDataSource {
    fun getAlbums(): Flow<List<Album>>
    fun getAlbumById(albumId: AlbumId): Flow<Album?>
    suspend fun saveAlbums(albums: List<Album>)
}