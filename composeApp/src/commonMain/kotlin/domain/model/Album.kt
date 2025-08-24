package domain.model


data class Album(
    val id: String,
    val name: String,
    val artistName: String,
    val artworkUrl: String,
    val releaseDate: String,
    val category: String,
    val albumUrl: String
)