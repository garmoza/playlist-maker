package com.practicum.playlistmaker.common.ui.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.transform.RoundedCornersTransformation
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.common.domain.models.Track
import kotlin.collections.forEach

private const val UNKNOWN_TRACK_NAME = "Track Unknown"
private const val UNKNOWN_ARTIST_NAME = "Artist Unknown"
private const val UNKNOWN_TIME = "--:--"

@Composable
fun Tracks(tracks: List<Track>, onTrackClick: (track: Track) -> Unit) {
    LazyColumn(
        modifier = Modifier.padding(top = 16.dp)
    ) {
        items(
            items = tracks,
            key = { track -> track.trackId },
            contentType = { track -> track::class }
        ) { track ->
            TrackListItem(track, onTrackClick)
        }
    }

    Column(
        modifier = Modifier.padding(top = 16.dp)
    ) { tracks.forEach { TrackListItem(it, onTrackClick) } }
}

@Composable
private fun TrackListItem(track: Track, onTrackClick: (track: Track) -> Unit) {
    val trackName = remember { track.trackName ?: UNKNOWN_TRACK_NAME }
    val artistName = remember { track.artistName ?: UNKNOWN_ARTIST_NAME }
    val trackTime = remember { track.trackTime ?: UNKNOWN_TIME }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 12.dp)
            .clickable { onTrackClick(track) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            modifier = Modifier.size(45.dp),
            model = ImageRequest.Builder(LocalContext.current)
                .data(track.artworkUrl100)
                .transformations(RoundedCornersTransformation(
                    with(LocalDensity.current) {
                        2.dp.toPx()
                    }
                ))
                .build(),
            placeholder = painterResource(R.drawable.placeholder_track_label),
            error = painterResource(R.drawable.placeholder_track_label),
            contentDescription = null
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(start = 8.dp)
        ) {
            Text(
                text = trackName,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.weight(1f, fill = false),
                    text = artistName,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Image(
                    painter = painterResource(R.drawable.ic_dot),
                    contentDescription = null,
                )

                Text(
                    text = trackTime,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Image(
            painter = painterResource(R.drawable.ic_arrow_forward),
            contentDescription = null
        )
    }
}