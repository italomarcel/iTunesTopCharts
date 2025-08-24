package domain.model

sealed interface AppResult<out T> {
    data class Success<T>(val data: T) : AppResult<T>
    data class Error(val error: AlbumError) : AppResult<Nothing>
    data object Loading : AppResult<Nothing>
}

val <T> AppResult<T>.isSuccess: Boolean get() = this is AppResult.Success
val <T> AppResult<T>.isError: Boolean get() = this is AppResult.Error
val <T> AppResult<T>.isLoading: Boolean get() = this is AppResult.Loading

inline fun <T> AppResult<T>.onSuccess(action: (T) -> Unit): AppResult<T> {
    if (this is AppResult.Success) action(data)
    return this
}

inline fun <T> AppResult<T>.onError(action: (AlbumError) -> Unit): AppResult<T> {
    if (this is AppResult.Error) action(error)
    return this
}