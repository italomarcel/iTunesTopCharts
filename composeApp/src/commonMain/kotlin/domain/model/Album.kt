package domain.model

import kotlinx.datetime.Instant

data class Album(
    val id: String,
    val name: String,
    val artistName: String,
    val artworkUrl: String,
    val releaseDate: String,
    val category: String,
    val albumUrl: String
)