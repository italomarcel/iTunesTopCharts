package domain.usecase

import domain.model.Album
import domain.model.AlbumId
import domain.model.AppResult
import data.local.AlbumsLocalDataSource
import domain.model.AlbumError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetAlbumDetailsUseCase(
    private val localDataSource: AlbumsLocalDataSource
) {
    operator fun invoke(albumId: AlbumId): Flow<AppResult<Album>> =
        localDataSource.getAlbumById(albumId)
            .map { album ->
                album?.let { AppResult.Success(it) }
                    ?: AppResult.Error(AlbumError.EmptyResponse)
            }
}