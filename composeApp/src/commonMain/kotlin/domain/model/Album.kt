package domain.model

import kotlinx.datetime.Instant

data class Album(
    val id: AlbumId,
    val name: String,
    val artist: ArtistName,
    val imageUrl: String,
    val releaseDate: Instant,
    val genre: String,
    val iTunesUrl: String
)
