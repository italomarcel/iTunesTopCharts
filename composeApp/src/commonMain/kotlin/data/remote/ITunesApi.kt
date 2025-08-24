package data.remote

import data.remote.dto.ITunesResponse
import domain.model.AppResult
import domain.model.AlbumError
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

class ITunesApi(private val client: HttpClient) : AlbumsRemoteDataSource {

    override suspend fun getTopAlbums(limit: Int): AppResult<ITunesResponse> {
        return try {
            val response: ITunesResponse = client.get {
                url {
                    takeFrom("https://itunes.apple.com/us/rss/topalbums/limit=$limit/json")
                }
            }.body()
            AppResult.Success(response)
        } catch (e: Exception) {
            AppResult.Error(
                AlbumError.NetworkError(e.message ?: "Network connection failed")
            )
        }
    }
}