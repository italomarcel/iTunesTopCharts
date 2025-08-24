package domain.model

import kotlin.jvm.JvmInline

@JvmInline
value class ArtistName private constructor(val value: String) {
    companion object {
        fun fromString(value: String?): ArtistName? =
            value?.takeIf { it.isNotBlank() }?.let { ArtistName(it) }
    }
}