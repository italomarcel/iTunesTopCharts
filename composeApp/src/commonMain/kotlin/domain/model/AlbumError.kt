package domain.model

sealed class AlbumError : Exception() {
    data class NetworkError(
        override val message: String = "Network connection failed"
    ) : AlbumError()

    data object EmptyResponse : AlbumError()
    data object CacheError : AlbumError()
    data object TimeoutError : AlbumError()

    data class ApiError(
        val code: Int? = null,
        override val message: String
    ) : AlbumError()

    data class ParseError(override val message: String) : AlbumError()

    fun toUserMessage(): String = when (this) {
        is NetworkError -> "Please check your internet connection"
        EmptyResponse -> "No albums found"
        CacheError -> "Please try again"
        TimeoutError -> "Connection timeout. Please try again"
        is ApiError -> "Service temporarily unavailable"
        is ParseError -> "Something went wrong. Please try again"
    }
}