package presentation.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import coil3.compose.AsyncImage
import domain.model.Album
import kotlinx.datetime.LocalDate

private object AlbumDetailScreenDefaults {
    val ContentPadding = 16.dp
    val ItemSpacing = 12.dp
    val ImageSize = 170.dp
    const val BackContentDescription = "Back"
    const val ArtistLabel = "Artist: "
    const val ReleaseDateLabel = "Release Date: "
}

private fun formatReleaseDate(date: String?): String =
    date?.substringBefore('T')?.let { datePart ->
        runCatching {
            val localDate = LocalDate.parse(datePart)
            val month =
                localDate.month.name.lowercase().replaceFirstChar(Char::titlecaseChar).take(3)
            "$month ${localDate.dayOfMonth}, ${localDate.year}"
        }.getOrDefault("-")
    } ?: "-"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlbumDetailScreen(
    album: Album,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(album.name ?: "-") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = AlbumDetailScreenDefaults.BackContentDescription
                        )
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
                .padding(AlbumDetailScreenDefaults.ContentPadding),
            contentAlignment = Alignment.Center
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(AlbumDetailScreenDefaults.ItemSpacing),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                album.artworkUrl?.let { url ->
                    AsyncImage(
                        model = url,
                        contentDescription = album.name ?: "-",
                        modifier = Modifier.size(AlbumDetailScreenDefaults.ImageSize)
                    )
                }
                Text(
                    text = "${AlbumDetailScreenDefaults.ArtistLabel}${album.artistName ?: "-"}",
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "${AlbumDetailScreenDefaults.ReleaseDateLabel}${formatReleaseDate(album.releaseDate)}",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}