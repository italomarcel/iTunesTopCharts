package domain.model

sealed class AlbumError {
    data object NetworkError : AlbumError()
    data object EmptyResponse : AlbumError()
    data class ApiError(val message: String) : AlbumError()
}