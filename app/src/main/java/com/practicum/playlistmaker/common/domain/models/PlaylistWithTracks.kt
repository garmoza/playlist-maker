package com.practicum.playlistmaker.common.domain.models

import android.net.Uri

data class PlaylistWithTracks(
    val id: Long? = null,
    val name: String,
    val description: String?,
    val label: Uri?,
    val tracks: List<Track>
) {
    constructor(
        playlist: Playlist,
        tracks: List<Track>
    ) : this (
        id = playlist.id,
        name = playlist.name,
        description = playlist.description,
        label = playlist.label,
        tracks = tracks
    )
}
