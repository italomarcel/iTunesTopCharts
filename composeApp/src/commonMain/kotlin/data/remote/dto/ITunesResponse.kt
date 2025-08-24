package data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ITunesResponse(
    val feed: Feed
)

@Serializable
data class Feed(
    val entry: List<AlbumDto> = emptyList(),
    val updated: Label? = null,
    val rights: Label? = null,
    val title: Label? = null
)

@Serializable
data class AlbumDto(
    val id: IdAttribute,
    @SerialName("im:name") val name: Label,
    @SerialName("im:artist") val artist: Artist,
    @SerialName("im:image") val images: List<Image>,
    @SerialName("im:releaseDate") val releaseDate: ReleaseDate,
    val category: Category,
    val link: Link,
    val title: Label? = null,
    @SerialName("im:itemCount") val itemCount: Label? = null,
    @SerialName("im:price") val price: Price? = null,
    val rights: Label? = null
)

@Serializable
data class IdAttribute(
    val label: String,
    val attributes: Map<String, String>
)

@Serializable
data class Label(
    val label: String
)

@Serializable
data class Artist(
    val label: String,
    val attributes: Map<String, String>? = null
)

@Serializable
data class Image(
    val label: String,
    val attributes: Map<String, String>? = null
)

@Serializable
data class ReleaseDate(
    val label: String,
    val attributes: Map<String, String>? = null
)

@Serializable
data class Category(
    val attributes: Map<String, String>
)

@Serializable
data class Link(
    val attributes: Map<String, String>
)

@Serializable
data class Price(
    val label: String,
    val attributes: Map<String, String>
)