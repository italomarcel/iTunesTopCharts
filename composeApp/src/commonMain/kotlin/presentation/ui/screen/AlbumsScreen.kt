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

private object AlbumsScreenDefaults {
    val ScreenPadding = 16.dp
    val SearchPadding = 8.dp
    val ItemSpacing = 8.dp
    val StateContentSpacing = 16.dp
    val StateContentPadding = 32.dp
    val RetryButtonSpacing = 8.dp

    const val MaxSearchLength = 100
    const val SearchPlaceholder = "Search albums or artists..."
    const val LoadingMessage = "Loading albums..."
    const val EmptyMessage = "No albums to display"
    const val RetryButtonText = "Try Again"
    const val SearchContentDescription = "Search"
    const val RefreshContentDescription = "Refresh albums"
}

@Composable
fun AlbumsScreen(
    uiState: AlbumsUiState,
    onSearchQueryChange: (String) -> Unit,
    onAlbumClick: (Album) -> Unit,
    onRefresh: () -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {
        AlbumsSearchBar(
            query = uiState.searchQuery, onQueryChange = { query ->
                if (query.length <= AlbumsScreenDefaults.MaxSearchLength) {
                    onSearchQueryChange(query)
                }
            }, modifier = Modifier.fillMaxWidth().padding(AlbumsScreenDefaults.SearchPadding)
        )

        AlbumsContent(
            uiState = uiState,
            onAlbumClick = onAlbumClick,
            onRefresh = onRefresh,
            onRetry = onRetry,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
private fun AlbumsContent(
    uiState: AlbumsUiState,
    onAlbumClick: (Album) -> Unit,
    onRefresh: () -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    when {
        uiState.isLoading && uiState.albums.isEmpty() -> {
            AlbumsLoadingState(modifier = modifier)
        }

        uiState.hasError -> {
            AlbumsErrorState(
                error = uiState.error, onRetry = onRetry, modifier = modifier
            )
        }

        uiState.isEmpty -> {
            AlbumsEmptyState(modifier = modifier)
        }

        else -> {
            AlbumsSuccessState(
                albums = uiState.filteredAlbums,
                onAlbumClick = onAlbumClick,
                onRefresh = onRefresh,
                modifier = modifier
            )
        }
    }
}

@Composable
private fun AlbumsSearchBar(
    query: String, onQueryChange: (String) -> Unit, modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        placeholder = { Text(AlbumsScreenDefaults.SearchPlaceholder) },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = AlbumsScreenDefaults.SearchContentDescription
            )
        },
        singleLine = true,
        modifier = modifier
    )
}

@Composable
private fun AlbumsSuccessState(
    albums: List<Album>,
    onAlbumClick: (Album) -> Unit,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(AlbumsScreenDefaults.ScreenPadding),
        verticalArrangement = Arrangement.spacedBy(AlbumsScreenDefaults.ItemSpacing)
    ) {
        item {
            AlbumsRefreshButton(onRefresh = onRefresh)
        }

        items(
            items = albums, key = { album -> album.id }) { album ->
            AlbumCard(
                album = album, onAlbumClick = onAlbumClick
            )
        }
    }
}

@Composable
private fun AlbumsRefreshButton(
    onRefresh: () -> Unit, modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End
    ) {
        IconButton(onClick = onRefresh) {
            Icon(
                imageVector = Icons.Default.Refresh,
                contentDescription = AlbumsScreenDefaults.RefreshContentDescription
            )
        }
    }
}

@Composable
private fun AlbumsLoadingState(
    modifier: Modifier = Modifier
) {
    CenteredStateContent(modifier = modifier) {
        CircularProgressIndicator()
        Text(
            text = AlbumsScreenDefaults.LoadingMessage, style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
private fun AlbumsErrorState(
    error: UiError?, onRetry: () -> Unit, modifier: Modifier = Modifier
) {
    CenteredStateContent(modifier = modifier) {
        Text(
            text = error.getErrorMessage(),
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center
        )

        Button(
            onClick = onRetry, modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.Default.Refresh, contentDescription = null
            )
            Spacer(modifier = Modifier.width(AlbumsScreenDefaults.RetryButtonSpacing))
            Text(AlbumsScreenDefaults.RetryButtonText)
        }
    }
}

@Composable
private fun AlbumsEmptyState(
    modifier: Modifier = Modifier
) {
    CenteredStateContent(modifier = modifier) {
        Text(
            text = AlbumsScreenDefaults.EmptyMessage,
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun CenteredStateContent(
    modifier: Modifier = Modifier, content: @Composable ColumnScope.() -> Unit
) {
    Box(
        modifier = modifier, contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(AlbumsScreenDefaults.StateContentSpacing),
            modifier = Modifier.padding(AlbumsScreenDefaults.StateContentPadding),
            content = content
        )
    }
}

private fun UiError?.getErrorMessage(): String = when (this) {
    is UiError.NetworkError -> "Network connection failed"
    is UiError.EmptyResponse -> "No albums found"
    is UiError.Unknown -> this.message
    null -> "Unknown error occurred"
}