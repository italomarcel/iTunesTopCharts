package domain.model

import kotlin.jvm.JvmInline

@JvmInline
value class AlbumId private constructor(val value: String) {

    init {
        require(value.isNotBlank()) { "AlbumId cannot be blank" }
    }

    companion object {
        fun fromString(value: String): AlbumId = AlbumId(value)

        fun fromStringOrNull(value: String?): AlbumId? =
            value?.takeIf { it.isNotBlank() }?.let { AlbumId(it) }
    }
}