package domain.model

sealed class AppResult<out T> {
    data class Success<T>(val data: T) : AppResult<T>()
    data class Error(val error: AlbumError) : AppResult<Nothing>()
    data object Loading : AppResult<Nothing>()
}