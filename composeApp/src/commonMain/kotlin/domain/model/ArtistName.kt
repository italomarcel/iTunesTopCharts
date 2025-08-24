package domain.model

import kotlin.jvm.JvmInline

@JvmInline
value class ArtistName(val value: String) {
    init {
        require(value.isNotBlank()) { "Artist name cannot be blank" }
    }
}