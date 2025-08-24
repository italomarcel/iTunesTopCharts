package presentation.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import domain.model.Album

private object AlbumCardDefaults {
    val CardElevation = 6.dp
    val CardPadding = 16.dp
    val ArtworkSize = 80.dp
    val ArtworkCornerRadius = 12.dp
    val ElementSpacing = 16.dp
    val TextSpacing = 6.dp
    val CategoryTopPadding = 4.dp
    val CategoryCornerRadius = 16.dp
    val CategoryHorizontalPadding = 8.dp
    val CategoryVerticalPadding = 4.dp

    const val ALBUM_NAME_MAX_LINES = 2
    const val ARTIST_NAME_MAX_LINES = 1
    const val CATEGORY_MAX_LINES = 1
}

@Composable
fun AlbumCard(
    album: Album, onAlbumClick: (Album) -> Unit, modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth().clickable(
            role = Role.Button, onClickLabel = "View album ${album.name}"
        ) { onAlbumClick(album) }, elevation = CardDefaults.cardElevation(
            defaultElevation = AlbumCardDefaults.CardElevation
        ), colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(AlbumCardDefaults.CardPadding),
            horizontalArrangement = Arrangement.spacedBy(AlbumCardDefaults.ElementSpacing),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AlbumArtwork(
                imageUrl = album.artworkUrl, albumName = album.name
            )

            AlbumInfo(
                album = album, modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun AlbumArtwork(
    imageUrl: String, albumName: String, modifier: Modifier = Modifier
) {
    AsyncImage(
        model = imageUrl,
        contentDescription = "Album artwork for $albumName",
        modifier = modifier.size(AlbumCardDefaults.ArtworkSize)
            .clip(RoundedCornerShape(AlbumCardDefaults.ArtworkCornerRadius)),
        contentScale = ContentScale.Crop
    )
}

@Composable
private fun AlbumInfo(
    album: Album, modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(AlbumCardDefaults.TextSpacing)
    ) {
        AlbumTitle(album.name)
        ArtistName(album.artistName)
        if (album.category.isNotEmpty()) {
            CategoryChip(album.category)
        }
    }
}

@Composable
private fun AlbumTitle(
    title: String, modifier: Modifier = Modifier
) {
    Text(
        text = title,
        modifier = modifier,
        style = MaterialTheme.typography.titleMedium.copy(
            fontWeight = FontWeight.SemiBold
        ),
        color = MaterialTheme.colorScheme.onSurface,
        maxLines = AlbumCardDefaults.ALBUM_NAME_MAX_LINES,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
private fun ArtistName(
    artistName: String, modifier: Modifier = Modifier
) {
    Text(
        text = artistName,
        modifier = modifier,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        maxLines = AlbumCardDefaults.ARTIST_NAME_MAX_LINES,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
private fun CategoryChip(
    category: String, modifier: Modifier = Modifier
) {
    Surface(
        color = MaterialTheme.colorScheme.primaryContainer,
        shape = RoundedCornerShape(AlbumCardDefaults.CategoryCornerRadius),
        modifier = modifier.padding(top = AlbumCardDefaults.CategoryTopPadding)
    ) {
        Text(
            text = category,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            modifier = Modifier.padding(
                horizontal = AlbumCardDefaults.CategoryHorizontalPadding,
                vertical = AlbumCardDefaults.CategoryVerticalPadding
            ),
            maxLines = AlbumCardDefaults.CATEGORY_MAX_LINES,
            overflow = TextOverflow.Ellipsis
        )
    }
}