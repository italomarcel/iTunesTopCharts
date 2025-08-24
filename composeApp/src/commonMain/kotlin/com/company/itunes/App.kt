package com.company.itunes

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import coil3.ImageLoader
import coil3.compose.setSingletonImageLoaderFactory
import coil3.network.ktor3.KtorNetworkFetcherFactory
import coil3.request.crossfade
import org.koin.compose.koinInject
import presentation.ui.screen.AlbumsScreen
import presentation.viewmodel.AlbumsViewModel

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

    AlbumsScreen(
        uiState = uiState,
        onSearchQueryChange = viewModel::updateSearchQuery,
        onAlbumClick = { album ->
            // TODO: Implement navigation
        },
        onRefresh = viewModel::refreshAlbums,
        onRetry = viewModel::retryLoadAlbums,
        modifier = modifier
    )
}