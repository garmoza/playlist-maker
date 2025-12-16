package com.practicum.playlistmaker.library.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.common.domain.models.Playlist
import com.practicum.playlistmaker.common.domain.models.Track
import com.practicum.playlistmaker.common.ui.compose.Toolbar
import com.practicum.playlistmaker.common.ui.debounceClick
import com.practicum.playlistmaker.favourite.ui.compose.FavoritesTracks
import com.practicum.playlistmaker.main.ui.theme.Theme
import kotlinx.coroutines.launch

@Composable
fun Library(
    onFavoriteTrackClick: (track: Track) -> Unit,
    onNewPlaylistButtonClick: () -> Unit,
    onPlaylistClick: (playlist: Playlist) -> Unit
) {
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState(pageCount = { 2 })
    val selectedTabIndex by remember {
        derivedStateOf { pagerState.currentPage }
    }

    val lifecycleOwner = LocalLifecycleOwner.current
    val lifecycleScope = lifecycleOwner.lifecycleScope

    val onFavoriteTrackDebounceClick: (track: Track) -> Unit = remember(lifecycleScope) {
        debounceClick(
            coroutineScope = lifecycleScope,
            action = { track -> onFavoriteTrackClick(track) }
        )
    }
    val onPlaylistDebounceClick: (playlist: Playlist) -> Unit = remember(lifecycleScope) {
        debounceClick(
            coroutineScope = lifecycleScope,
            action = { playlist -> onPlaylistClick(playlist) }
        )
    }

    Column(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surface)
    ) {
        Toolbar(R.string.library)

        TabRow(
            selectedTabIndex = selectedTabIndex,
            modifier = Modifier.fillMaxWidth(),
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier
                        .tabIndicatorOffset(tabPositions[selectedTabIndex])
                        .padding(horizontal = 16.dp),
                    height = 2.dp,
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            divider = {}
        ) {
            Tab(
                selected = selectedTabIndex == 0,
                onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(0)
                    }
                },
                text = {
                    Text(
                        text = stringResource(R.string.favorites_tracks),
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            )

            Tab(
                selected = selectedTabIndex == 1,
                onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(1)
                    }
                },
                text = {
                    Text(
                        text = stringResource(R.string.playlists),
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            )
        }

        HorizontalPager(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalAlignment = Alignment.Top,
            state = pagerState
        ) { page ->
            when (page) {
                0 -> FavoritesTracks(
                    onTrackClick = onFavoriteTrackDebounceClick
                )
                1 -> CreatedPlaylists(
                    onNewPlaylistButtonClick = onNewPlaylistButtonClick,
                    onPlaylistClick = onPlaylistDebounceClick
                )
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun LibraryPreviewWithTheme() {
    Theme {
        Library(
            onFavoriteTrackClick = {},
            onNewPlaylistButtonClick = {},
            onPlaylistClick = {}
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun LibraryPreview() {
    Library(
        onFavoriteTrackClick = {},
        onNewPlaylistButtonClick = {},
        onPlaylistClick = {}
    )
}