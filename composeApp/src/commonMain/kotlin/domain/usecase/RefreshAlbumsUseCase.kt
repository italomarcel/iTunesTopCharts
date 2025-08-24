package domain.usecase

import domain.model.AppResult
import domain.model.Album
import domain.repository.AlbumsRepository

class RefreshAlbumsUseCase(
    private val repository: AlbumsRepository
) {
    suspend operator fun invoke(): AppResult<List<Album>> =
        repository.refreshAlbums()
}