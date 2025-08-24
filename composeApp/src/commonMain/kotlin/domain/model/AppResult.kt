package domain.model

import kotlin.jvm.JvmInline

@JvmInline
value class AlbumId(val value: String) {
    init {
        require(value.isNotBlank()) { "Album ID cannot be blank" }
    }
}
