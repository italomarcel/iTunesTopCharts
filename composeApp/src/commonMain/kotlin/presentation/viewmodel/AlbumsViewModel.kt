package presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import domain.model.AppResult
import domain.repository.AlbumsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import presentation.state.AlbumsUiState
import presentation.state.UiError

class AlbumsViewModel(
    private val repository: AlbumsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AlbumsUiState())
    val uiState: StateFlow<AlbumsUiState> = _uiState.asStateFlow()

    init {
        loadAlbums()
    }

    private fun loadAlbums() {
        viewModelScope.launch {
            repository.getTopAlbums().collect { result ->
                _uiState.value = when (result) {
                    is AppResult.Loading -> {
                        _uiState.value.copy(isLoading = true, error = null)
                    }

                    is AppResult.Success -> {
                        _uiState.value.copy(
                            isLoading = false, albums = result.data, error = null
                        )
                    }

                    is AppResult.Error -> {
                        _uiState.value.copy(
                            isLoading = false, error = UiError.fromAlbumError(result.error)
                        )
                    }
                }
            }
        }
    }

    fun refreshAlbums() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isRefreshing = true)
            when (val result = repository.refreshAlbums()) {
                is AppResult.Success -> {
                    _uiState.value = _uiState.value.copy(
                        albums = result.data, isRefreshing = false, error = null
                    )
                }

                is AppResult.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isRefreshing = false, error = UiError.fromAlbumError(result.error)
                    )
                }

                is AppResult.Loading -> {}
            }
        }
    }

    fun updateSearchQuery(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
    }

    fun retryLoadAlbums() {
        _uiState.value = _uiState.value.copy(error = null)
        loadAlbums()
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}