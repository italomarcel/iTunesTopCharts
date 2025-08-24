package domain.usecase

import domain.model.AppResult
import domain.model.Album
import domain.repository.AlbumsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RefreshAlbumsUseCase(
    private val repository: AlbumsRepository
) {
    operator fun invoke(): Flow<AppResult<List<Album>>> = flow {
        emit(AppResult.Loading)
        emit(repository.refreshAlbums())
    }
}