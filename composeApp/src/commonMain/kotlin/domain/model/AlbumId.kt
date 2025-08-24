package domain.model

import kotlin.jvm.JvmInline

@JvmInline
value class AlbumId private constructor(val value: String) {
    companion object {
        fun fromString(value: String?): AlbumId? =
            value?.takeIf { it.isNotBlank() }?.let(::AlbumId)
    }
}