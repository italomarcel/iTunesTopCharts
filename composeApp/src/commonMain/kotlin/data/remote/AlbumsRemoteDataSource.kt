package data.remote

import data.remote.dto.ITunesResponse
import domain.model.AppResult

interface AlbumsRemoteDataSource {
    suspend fun getTopAlbums(limit: Int = 100): AppResult<ITunesResponse>
}