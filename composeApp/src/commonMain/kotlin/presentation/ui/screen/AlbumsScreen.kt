package presentation.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import domain.model.Album
import presentation.state.AlbumsUiState
import presentation.state.UiError
import presentation.ui.component.AlbumCard

private object AlbumsScreenDimensions {
    val screenPadding = 16.dp
    val searchBarPadding = 8.dp
    val itemSpacing = 8.dp
    val errorPadding = 32.dp
    val loadingPadding = 64.dp
    val emptyStatePadding = 48.dp
}

private object AlbumsScreenLimits {
    const val maxSearchQueryLength = 100
}

@Composable
fun AlbumsScreen(
    uiState: AlbumsUiState,
    onLoadAlbums: () -> Unit,
    onRefreshAlbums: () -> Unit,
    onSearchQueryChange: (String) -> Unit,
    onAlbumClick: (Album) -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        SearchBar(
            query = uiState.searchQuery,
            onQueryChange = { query ->
                if (query.length <= AlbumsScreenLimits.maxSearchQueryLength) {
                    onSearchQueryChange(query)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(AlbumsScreenDimensions.searchBarPadding)
        )

        when {
            uiState.isLoading && uiState.albums.isEmpty() -> {
                LoadingState(
                    modifier = Modifier.fillMaxSize()
                )
            }

            uiState.hasError -> {
                ErrorState(
                    error = uiState.error,
                    onRetry = onRetry,
                    modifier = Modifier.fillMaxSize()
                )
            }

            uiState.isEmpty -> {
                EmptyState(
                    modifier = Modifier.fillMaxSize()
                )
            }

            else -> {
                AlbumsList(
                    albums = uiState.filteredAlbums,
                    onAlbumClick = onAlbumClick,
                    onRefresh = onRefreshAlbums,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Composable
private fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        placeholder = { Text("Search albums or artists...") },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search icon"
            )
        },
        singleLine = true,
        modifier = modifier
    )
}

@Composable
private fun AlbumsList(
    albums: List<Album>,
    onAlbumClick: (Album) -> Unit,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(AlbumsScreenDimensions.screenPadding),
        verticalArrangement = Arrangement.spacedBy(AlbumsScreenDimensions.itemSpacing)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(onClick = onRefresh) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Refresh"
                    )
                }
            }
        }

        items(
            items = albums,
            key = { album -> album.id }
        ) { album ->
            AlbumCard(
                album = album,
                onAlbumClick = onAlbumClick
            )
        }
    }
}

@Composable
private fun LoadingState(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(AlbumsScreenDimensions.loadingPadding)
        ) {
            CircularProgressIndicator()
            Text(
                text = "Loading albums...",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Composable
private fun ErrorState(
    error: UiError?,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(AlbumsScreenDimensions.errorPadding),
            modifier = Modifier.padding(AlbumsScreenDimensions.errorPadding)
        ) {
            Text(
                text = when (error) {
                    is UiError.NetworkError -> "Network connection failed"
                    is UiError.EmptyResponse -> "No albums found"
                    is UiError.Unknown -> error.message
                    null -> "Unknown error occurred"
                },
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center
            )

            Button(
                onClick = onRetry,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(AlbumsScreenDimensions.searchBarPadding))
                Text("Try Again")
            }
        }
    }
}

@Composable
private fun EmptyState(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "No albums to display",
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(AlbumsScreenDimensions.emptyStatePadding)
        )
    }
}