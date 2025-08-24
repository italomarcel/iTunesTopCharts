package presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import domain.model.AppResult
import domain.repository.AlbumsRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.CancellationException
import presentation.state.AlbumsUiState
import presentation.state.UiError
import utils.runSuspendCatching

class AlbumsViewModel(
    private val repository: AlbumsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AlbumsUiState())
    val uiState: StateFlow<AlbumsUiState> = _uiState.asStateFlow()

    private var loadJob: Job? = null
    private var refreshJob: Job? = null

    init {
        loadAlbums()
    }

    private fun loadAlbums() {
        loadJob?.cancel()
        loadJob = repository.getTopAlbums()
            .distinctUntilChanged()
            .onStart {
                _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            }
            .catch { exception ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = UiError.fromAlbumError(
                        domain.model.AlbumError.NetworkError(
                            exception.message ?: "Unexpected error"
                        )
                    )
                )
            }
            .onEach { result ->
                _uiState.value = when (result) {
                    is AppResult.Loading -> _uiState.value.copy(isLoading = true, error = null)
                    is AppResult.Success -> _uiState.value.copy(
                        isLoading = false,
                        albums = result.data,
                        error = null
                    )

                    is AppResult.Error -> _uiState.value.copy(
                        isLoading = false,
                        error = UiError.fromAlbumError(result.error)
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    fun refreshAlbums() {
        if (refreshJob?.isActive == true) return

        refreshJob = viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isRefreshing = true)

            runSuspendCatching { repository.refreshAlbums() }
                .onSuccess { result ->
                    _uiState.value = when (result) {
                        is AppResult.Success -> _uiState.value.copy(
                            albums = result.data,
                            isRefreshing = false,
                            error = null
                        )

                        is AppResult.Error -> _uiState.value.copy(
                            isRefreshing = false,
                            error = UiError.fromAlbumError(result.error)
                        )

                        is AppResult.Loading -> _uiState.value.copy(isRefreshing = false)
                    }
                }
                .onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        isRefreshing = false,
                        error = UiError.fromAlbumError(
                            domain.model.AlbumError.NetworkError(
                                exception.message ?: "Refresh failed"
                            )
                        )
                    )
                }
        }
    }

    fun updateSearchQuery(query: String) {
        val validatedQuery = query.take(MAX_SEARCH_LENGTH).trim()
        if (_uiState.value.searchQuery != validatedQuery) {
            _uiState.value = _uiState.value.copy(searchQuery = validatedQuery)
        }
    }

    fun retryLoadAlbums() {
        _uiState.value = _uiState.value.copy(error = null)
        loadAlbums()
    }

    override fun onCleared() {
        super.onCleared()
        loadJob?.cancel()
        refreshJob?.cancel()
    }

    companion object {
        private const val MAX_SEARCH_LENGTH = 100
    }
}
