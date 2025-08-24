package domain.usecase

import domain.model.Album
import domain.model.AlbumId
import domain.model.AppResult
import domain.repository.AlbumsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetAlbumDetailsUseCase(
    private val repository: AlbumsRepository
) {
    operator fun invoke(albumId: AlbumId): Flow<AppResult<Album?>> =
        repository.getTopAlbums().map { result ->
            when (result) {
                is AppResult.Success -> {
                    val album = result.data.find { it.id == albumId }
                    AppResult.Success(album)
                }

                is AppResult.Error -> result
                AppResult.Loading -> AppResult.Loading
            }
        }
}