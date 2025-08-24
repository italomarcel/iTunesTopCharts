package data.remote.mapper

import data.remote.dto.AlbumDto
import domain.model.Album

object AlbumMapper {
    fun AlbumDto.toDomain(): Album? = runCatching {
        val imageUrl = images.lastOrNull()?.label.orEmpty()

        Album(
            id = id.attributes["im:id"].orEmpty(),
            name = name.label,
            artistName = artist.label,
            artworkUrl = imageUrl,
            releaseDate = releaseDate.label,
            category = category.attributes["label"].orEmpty(),
            albumUrl = link.attributes["href"].orEmpty()
        )
    }.getOrNull()
}