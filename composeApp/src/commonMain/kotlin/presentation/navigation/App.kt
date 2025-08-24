package presentation.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import coil3.ImageLoader
import coil3.compose.setSingletonImageLoaderFactory
import coil3.network.ktor3.KtorNetworkFetcherFactory
import coil3.request.crossfade
import org.koin.compose.koinInject
import presentation.ui.screen.AlbumsScreen
import presentation.ui.screen.AlbumDetailScreen
import presentation.viewmodel.AlbumsViewModel
import domain.model.Album

@Composable
fun App() {
    ConfigureImageLoader()

    MaterialTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize()
        ) { paddingValues ->
            AlbumsRoute(
                modifier = Modifier.padding(paddingValues)
            )
        }
    }
}

@Composable
private fun ConfigureImageLoader() {
    setSingletonImageLoaderFactory { context ->
        ImageLoader.Builder(context)
            .components {
                add(KtorNetworkFetcherFactory())
            }
            .crossfade(enable = true)
            .build()
    }
}

@Composable
private fun AlbumsRoute(
    modifier: Modifier = Modifier,
    viewModel: AlbumsViewModel = koinInject()
) {
    val uiState by viewModel.uiState.collectAsState()
    var selectedAlbum by remember { mutableStateOf<Album?>(null) }

    if (selectedAlbum == null) {
        AlbumsScreen(
            uiState = uiState,
            onSearchQueryChange = viewModel::updateSearchQuery,
            onAlbumClick = { album -> selectedAlbum = album },
            onRefresh = viewModel::refreshAlbums,
            onRetry = viewModel::retryLoadAlbums,
            modifier = modifier
        )
    } else {
        selectedAlbum?.let { album ->
            AlbumDetailScreen(
                album = album,
                onBack = { selectedAlbum = null }
            )
        }
    }
}