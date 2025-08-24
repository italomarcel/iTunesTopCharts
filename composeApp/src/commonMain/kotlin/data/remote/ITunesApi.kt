package data.remote

import data.remote.dto.ITunesResponse
import domain.model.AppResult
import domain.model.AlbumError
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.plugins.*
import io.ktor.http.*
import kotlinx.coroutines.TimeoutCancellationException

class ITunesApi(private val client: HttpClient) : AlbumsRemoteDataSource {

    override suspend fun getTopAlbums(limit: Int): AppResult<ITunesResponse> = runCatching {
        client.get("https://itunes.apple.com/us/rss/topalbums/limit=$limit/json")
            .body<ITunesResponse>()
    }.fold(
        onSuccess = { AppResult.Success(it) },
        onFailure = { AppResult.Error(it.toAlbumError()) })

    private fun Throwable.toAlbumError(): AlbumError = when (this) {
        is TimeoutCancellationException -> AlbumError.NetworkError("Request timeout")
        is ClientRequestException -> AlbumError.NetworkError(
            when (response.status.value) {
                400 -> "Invalid request"
                404 -> "Resource not found"
                else -> "Client error: ${response.status.value}"
            }
        )

        is ServerResponseException -> AlbumError.NetworkError("Server error: ${response.status.value}")
        is RedirectResponseException -> AlbumError.NetworkError("Redirect error")
        else -> AlbumError.NetworkError(message ?: "Unknown network error")
    }
}