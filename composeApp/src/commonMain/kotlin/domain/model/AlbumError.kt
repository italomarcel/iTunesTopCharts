package domain.model

sealed class AlbumError : Exception() {
    data object NetworkError : AlbumError()
    data object EmptyResponse : AlbumError()
    data object CacheError : AlbumError()
    data object TimeoutError : AlbumError()
    data class ApiError(
        val code: Int? = null,
        override val message: String
    ) : AlbumError()
    data class ParseError(override val message: String) : AlbumError()

    fun toUserMessage(): String = when (this) {
        NetworkError -> "Please check your internet connection"
        EmptyResponse -> "No albums found"
        CacheError -> "Failed to load cached data"
        TimeoutError -> "Connection timeout. Please try again"
        is ApiError -> "Server error: $message"
        is ParseError -> "Failed to process data"
    }
}