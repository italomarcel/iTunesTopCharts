package data.local

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.company.itunes.database.Albums
import com.company.itunes.database.iTunesAlbumsDatabase
import domain.model.*
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class SQLDelightLocalDataSource(
    private val database: iTunesAlbumsDatabase
) : AlbumsLocalDataSource {

    override fun getAlbums(): Flow<List<Album>> =
        database.albumsQueries.selectAllAlbums(limit = DEFAULT_LIMIT).asFlow().mapToList(Default)
            .map { entities -> entities.map { it.toDomain() } }.catch { emit(emptyList()) }

    override fun getAlbumById(albumId: String): Flow<Album?> =
        database.albumsQueries.selectAlbumById(albumId).asFlow().mapToOneOrNull(Default)
            .map { it?.toDomain() }.catch { emit(null) }

    override suspend fun saveAlbums(albums: List<Album>) {
        database.transaction {
            database.albumsQueries.deleteAllAlbums()
            albums.forEach { album ->
                database.albumsQueries.insertAlbum(
                    album.id,
                    album.name,
                    album.artistName,
                    album.artworkUrl,
                    album.releaseDate,
                    album.category,
                    album.albumUrl
                )
            }
        }
    }

    companion object {
        private const val DEFAULT_LIMIT = 100L
    }
}

private fun Albums.toDomain(): Album = Album(
    id = id,
    name = name,
    artistName = artistName,
    artworkUrl = artworkUrl,
    releaseDate = releaseDate,
    category = category,
    albumUrl = albumUrl
)