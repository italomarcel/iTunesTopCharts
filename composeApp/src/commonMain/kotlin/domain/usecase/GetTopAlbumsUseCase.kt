package domain.usecase

import domain.model.AppResult
import domain.model.Album
import domain.repository.AlbumsRepository
import kotlinx.coroutines.flow.Flow

class GetTopAlbumsUseCase(
    private val repository: AlbumsRepository
) {
    operator fun invoke(): Flow<AppResult<List<Album>>> =
        repository.getTopAlbums()
}