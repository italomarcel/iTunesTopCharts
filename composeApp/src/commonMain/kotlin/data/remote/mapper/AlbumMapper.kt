package data.remote.mapper

import data.remote.dto.AlbumDto
import domain.model.*
import kotlinx.datetime.Instant

object AlbumMapper {

    fun List<AlbumDto>.toDomain(): List<Album> = mapNotNull { it.toDomain() }

    private fun AlbumDto.toDomain(): Album? = runCatching {
        Album(
            id = AlbumId.fromStringOrNull(id.attributes?.getValue("im:id")) ?: return null,
            name = name.label,
            artist = ArtistName.fromString(artist.label),
            imageUrl = images.lastOrNull()?.label ?: return null,
            releaseDate = releaseDate.label.runCatching(Instant::parse)
                .getOrDefault(Instant.DISTANT_PAST),
            genre = category.attributes?.get("label").orEmpty(),
            iTunesUrl = link.attributes?.get("href").orEmpty()
        )
    }.getOrNull()
}