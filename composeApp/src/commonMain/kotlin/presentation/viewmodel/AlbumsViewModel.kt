package presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import presentation.state.AlbumsUiState
import presentation.state.UiError

class AlbumsViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(AlbumsUiState())
    val uiState: StateFlow<AlbumsUiState> = _uiState.asStateFlow()

    init {
        loadAlbums()
    }

    fun loadAlbums() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            try {
                // TODO: Implementar chamada para o use case quando estiver disponível
                // Por enquanto, simular carregamento
                kotlinx.coroutines.delay(2000)
                _uiState.value = _uiState.value.copy(
                    albums = emptyList(),
                    isLoading = false,
                    error = null
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = UiError.Unknown(e.message ?: "Unknown error")
                )
            }
        }
    }

    fun refreshAlbums() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isRefreshing = true)

            try {
                // TODO: Implementar refresh quando use case estiver disponível
                kotlinx.coroutines.delay(1000)
                _uiState.value = _uiState.value.copy(
                    albums = emptyList(),
                    isRefreshing = false,
                    error = null
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isRefreshing = false,
                    error = UiError.Unknown(e.message ?: "Refresh error")
                )
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