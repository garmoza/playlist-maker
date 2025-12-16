package com.practicum.playlistmaker.favourite.ui.compose

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.common.domain.models.Track
import com.practicum.playlistmaker.common.ui.compose.ContentPlaceholder
import com.practicum.playlistmaker.common.ui.compose.Loader
import com.practicum.playlistmaker.common.ui.compose.Tracks
import com.practicum.playlistmaker.favourite.domain.model.FavoritesTracksScreenState
import com.practicum.playlistmaker.favourite.ui.view_model.FavoritesTracksViewModel
import com.practicum.playlistmaker.main.ui.theme.Theme
import org.koin.androidx.compose.koinViewModel

@Composable
fun FavoritesTracks(
    onTrackClick: (track: Track) -> Unit
) {
    val viewModel: FavoritesTracksViewModel = koinViewModel()

    val screenState by viewModel.getLiveData()
        .observeAsState(FavoritesTracksScreenState.Loading)

    LaunchedEffect(Unit) {
        viewModel.getFavouriteTracks()
    }

    FavoritesTracks(
        screenState = screenState,
        onTrackClick = onTrackClick
    )
}

@Composable
private fun FavoritesTracks(
    screenState: FavoritesTracksScreenState,
    onTrackClick: (track: Track) -> Unit
) {
    when (screenState) {
        is FavoritesTracksScreenState.Loading -> Loader()
        is FavoritesTracksScreenState.Content -> {
            if (screenState.tracks.isEmpty()) {
                ContentPlaceholder(
                    modifier = Modifier.padding(top = 106.dp),
                    imageId = R.drawable.track_not_found,
                    messageId = R.string.your_library_is_empty
                )
            } else {
                Tracks(screenState.tracks, onTrackClick)
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun FavoritesTracksLoaderPreview() {
    Theme {
        FavoritesTracks(
            screenState = FavoritesTracksScreenState.Loading,
            onTrackClick = {}
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun FavoritesTracksNotFoundPreview() {
    Theme {
        FavoritesTracks(
            screenState = FavoritesTracksScreenState.Content(emptyList()),
            onTrackClick = {}
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun FavoritesTracksContentPreview() {
    Theme {
        FavoritesTracks(
            screenState = FavoritesTracksScreenState.Content(
                listOf(DEFAULT_TRACK, LONG_TEXT_TRACK, FULL_FILLED_TRACK)
            ),
            onTrackClick = {}
        )
    }
}

private val DEFAULT_TRACK = Track(
    trackId = "12345",
    trackName = "Yesterday (Remastered 2009)",
    artistName = "The Beatles",
    trackTimeMillis = 335_000L,
    artworkUrl100 = null,
    collectionName = "Yesterday (Remastered 2009)",
    releaseDate = "1965",
    primaryGenreName = "Rock",
    country = "United Kingdom",
    previewUrl = null,
    isFavorite = true
)

private val LONG_TEXT_TRACK = Track(
    trackId = "6789",
    trackName = "Text Very Long Long Long Long Long Long Long Long Long Long Long Long Long",
    artistName = "Text Very Long Long Long Long Long Long Long Long Long Long Long Long Long",
    trackTimeMillis = 335_000L,
    artworkUrl100 = null,
    collectionName = "Yesterday (Remastered 2009)",
    releaseDate = "1965",
    primaryGenreName = "Rock",
    country = "United Kingdom",
    previewUrl = null,
    isFavorite = true
)

private val FULL_FILLED_TRACK = Track(
    trackId = "1803726253",
    trackName = "nice",
    artistName = "2hollis",
    trackTimeMillis = 142493L,
    artworkUrl100 = "https://is1-ssl.mzstatic.com/image/thumb/Music211/v4/3a/da/c0/3adac065-136d-d93c-edc8-fdfa4a744b7f/25UMGIM44740.rgb.jpg/100x100bb.jpg",
    collectionName = "star",
    releaseDate = "2025-04-04T12:00:00Z",
    primaryGenreName = "Pop",
    country = "USA",
    previewUrl = "https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview211/v4/d7/94/68/d79468b6-c08c-21cf-7d5a-9ad052617b17/mzaf_16002216340131945413.plus.aac.p.m4a",
    isFavorite = true
)
