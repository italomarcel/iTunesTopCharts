package data.remote.mapper

import data.remote.dto.AlbumDto
import domain.model.Album

object AlbumMapper {

    fun List<AlbumDto>.toDomain(): List<Album> = mapNotNull { it.toDomain() }

    private fun AlbumDto.toDomain(): Album? = runCatching {
        Album(
            id = id.attributes?.getValue("im:id") ?: return null,
            name = name.label,
            artistName = artist.label,
            artworkUrl = images.lastOrNull()?.label ?: return null,
            releaseDate = releaseDate.label,
            category = category.attributes?.get("label").orEmpty(),
            albumUrl = link.attributes?.get("href").orEmpty()
        )
    }.getOrNull()
}