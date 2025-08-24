package presentation.state

import domain.model.Album

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
    object NetworkError : UiError()
    object EmptyResponse : UiError()
    data class Unknown(val message: String) : UiError()
}