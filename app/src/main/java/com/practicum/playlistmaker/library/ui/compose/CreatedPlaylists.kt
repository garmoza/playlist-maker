package com.practicum.playlistmaker.library.ui.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.transform.RoundedCornersTransformation
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.common.domain.models.Playlist
import com.practicum.playlistmaker.common.ui.compose.RoundedButton
import com.practicum.playlistmaker.library.domain.model.PlaylistsScreenState
import com.practicum.playlistmaker.library.ui.view_model.PlaylistsViewModel
import com.practicum.playlistmaker.main.ui.theme.Theme
import org.koin.androidx.compose.koinViewModel

@Composable
fun CreatedPlaylists(
    onNewPlaylistButtonClick: () -> Unit,
    onPlaylistClick: (playlist: Playlist) -> Unit
) {
    val viewModel: PlaylistsViewModel = koinViewModel()

    val screenState by viewModel.getLiveData()
        .observeAsState(PlaylistsScreenState.Content(emptyList()))

    LaunchedEffect(Unit) {
        viewModel.loadPlaylists()
    }

    CreatedPlaylists(
        screenState = screenState,
        onNewPlaylistButtonClick = onNewPlaylistButtonClick,
        onPlaylistClick = onPlaylistClick
    )
}

@Composable
private fun CreatedPlaylists(
    screenState: PlaylistsScreenState,
    onNewPlaylistButtonClick: () -> Unit,
    onPlaylistClick: (playlist: Playlist) -> Unit
) {
    when (screenState) {
        is PlaylistsScreenState.Content -> Playlists(
            playlists = screenState.playlists,
            onNewPlaylistButtonClick = onNewPlaylistButtonClick,
            onPlaylistClick = onPlaylistClick
        )
    }
}

@Composable
private fun Playlists(
    playlists: List<Playlist>,
    onNewPlaylistButtonClick: () -> Unit,
    onPlaylistClick: (playlist: Playlist) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface),
        contentAlignment = Alignment.TopCenter
    ) {
        RoundedButton(
            modifier = Modifier.padding(top = 24.dp),
            textId = R.string.new_playlist,
            onClick = onNewPlaylistButtonClick
        )

        if (playlists.isEmpty()) {
            CreatedPlaylistsNotFound()
        } else {
            LazyVerticalGrid(
                modifier = Modifier.padding(top = 76.dp),
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(items = playlists) { playlist ->
                    PlaylistGridItem(playlist, onPlaylistClick)
                }
            }
        }
    }
}

@Composable
private fun CreatedPlaylistsNotFound() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 106.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(R.drawable.track_not_found),
            contentDescription = stringResource(R.string.you_have_not_playlists)
        )

        Text(
            modifier = Modifier.padding(16.dp),
            textAlign = TextAlign.Center,
            text = stringResource(R.string.you_have_not_playlists),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun PlaylistGridItem(
    playlist: Playlist,
    onPlaylistClick: (playlist: Playlist) -> Unit
) {
    val context = LocalContext.current

    val numberOfTracks = remember {
        context.resources.getQuantityString(
            R.plurals.number_of_tracks,
            playlist.trackIds.size,
            playlist.trackIds.size
        )
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onPlaylistClick(playlist) }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
        ) {
            AsyncImage(
                modifier = Modifier.fillMaxSize(),
                model = ImageRequest.Builder(LocalContext.current)
                    .data(playlist.label)
                    .transformations(RoundedCornersTransformation(
                        with(LocalDensity.current) {
                            8.dp.toPx()
                        }
                    ))
                    .build(),
                placeholder = painterResource(R.drawable.placeholder_track_label),
                error = painterResource(R.drawable.placeholder_track_label),
                contentDescription = null
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = playlist.name,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface
        )

        Text(
            text = numberOfTracks,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun CreatedPlaylistsPreviewLightTheme() {
    Theme(false) {
        CreatedPlaylists(
            screenState = PlaylistsScreenState.Content(
                listOf(PLAYLIST_1, PLAYLIST_2, PLAYLIST_3)
            ),
            onNewPlaylistButtonClick = {},
            onPlaylistClick = {},
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun CreatedPlaylistsPreviewDarkTheme() {
    Theme(true) {
        CreatedPlaylists(
            screenState = PlaylistsScreenState.Content(
                listOf(PLAYLIST_1, PLAYLIST_2, PLAYLIST_3)
            ),
            onNewPlaylistButtonClick = {},
            onPlaylistClick = {},
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun CreatedPlaylistsNotFoundPreviewLightTheme() {
    Theme(false) {
        CreatedPlaylists(
            screenState = PlaylistsScreenState.Content(
                emptyList()
            ),
            onNewPlaylistButtonClick = {},
            onPlaylistClick = {},
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun CreatedPlaylistsNotFoundPreviewDarkTheme() {
    Theme(true) {
        CreatedPlaylists(
            screenState = PlaylistsScreenState.Content(
                emptyList()
            ),
            onNewPlaylistButtonClick = {},
            onPlaylistClick = {},
        )
    }
}

private val PLAYLIST_1 = Playlist(
    id = 1,
    name = "BeSt SoNg EvEr!",
    description = "Best Description",
    label = null,
    trackIds = listOf("1")
)

private val PLAYLIST_2 = Playlist(
    id = 2,
    name = "Text Very Long Long Long Long Long Long Long Long Long Long Long Long Long",
    description = null,
    label = null,
    trackIds = listOf("1", "2")
)

private val PLAYLIST_3 = Playlist(
    id = 3,
    name = "Summer Party",
    description = null,
    label = null,
    trackIds = emptyList()
)