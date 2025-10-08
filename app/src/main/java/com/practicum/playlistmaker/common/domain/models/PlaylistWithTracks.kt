package com.practicum.playlistmaker.common.domain.models

data class PlaylistWithTracks(
    val playlist: Playlist,
    val tracks: List<Track>
) {
    val totalDurationMillis: Long
        get() = tracks.sumOf { it.trackTimeMillis ?: 0L }
}
