package domain.model

import kotlin.jvm.JvmInline

@JvmInline
value class ArtistName private constructor(val value: String) {

    init {
        require(value.isNotBlank()) { "Artist name cannot be blank" }
    }

    companion object {
        fun fromString(value: String): ArtistName = ArtistName(value)

    }
}