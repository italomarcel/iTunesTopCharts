package data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ITunesResponse(
    val feed: Feed
)

@Serializable
data class Feed(
    val results: List<AlbumDto>
)

@Serializable
data class AlbumDto(
    val id: Attributes,
    @SerialName("im:name") val name: Label,
    @SerialName("im:artist") val artist: Label,
    @SerialName("im:image") val images: List<Image>,
    @SerialName("im:releaseDate") val releaseDate: Label,
    val category: Attributes,
    val link: Attributes
)

@Serializable
data class Attributes(
    val attributes: Map<String, String>? = null,
    val label: String? = null
)

@Serializable
data class Label(val label: String)

@Serializable
data class Image(
    val label: String,
    val attributes: Map<String, String>? = null
)