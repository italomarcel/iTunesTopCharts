package data.remote

import data.remote.dto.ITunesResponse
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*

class ITunesApi(private val client: HttpClient) {

    suspend fun getTopAlbums(): ITunesResponse =
        client.get(ITUNES_TOP_ALBUMS_URL).body()

    companion object Companion {
        const val ITUNES_TOP_ALBUMS_URL = "https://itunes.apple.com/us/rss/topalbums/limit=100/json"
    }
}