package presentation.state

import domain.model.Album
import domain.model.AlbumError

data class AlbumsUiState(
    val albums: List<Album> = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val searchQuery: String = "",
    val error: UiError? = null
) {
    val hasError: Boolean get() = error != null
    val isEmpty: Boolean get() = albums.isEmpty() && !isLoading
    val hasContent: Boolean get() = albums.isNotEmpty()
    val filteredAlbums: List<Album>
        get() =
            if (searchQuery.isBlank()) albums
            else albums.filter {
                it.name.contains(searchQuery, ignoreCase = true) ||
                        it.artistName.contains(searchQuery, ignoreCase = true)
            }
}

sealed class UiError {
    data object NetworkError : UiError()
    data object EmptyResponse : UiError()
    data class Unknown(val message: String) : UiError()

    companion object {
        fun fromAlbumError(albumError: AlbumError): UiError = when (albumError) {
            is AlbumError.NetworkError -> NetworkError
            is AlbumError.TimeoutError -> NetworkError
            is AlbumError.EmptyResponse -> EmptyResponse
            is AlbumError.ApiError -> Unknown("Server error: ${albumError.message}")
            is AlbumError.ParseError -> Unknown("Data parsing error")
            is AlbumError.CacheError -> Unknown("Cache error")
        }
    }
}