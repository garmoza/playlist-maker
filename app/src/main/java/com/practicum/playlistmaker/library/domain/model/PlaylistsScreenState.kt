package com.practicum.playlistmaker.library.domain.model

import com.practicum.playlistmaker.common.domain.models.Playlist

sealed interface PlaylistsScreenState {
    data class Content(
        val playlists: List<Playlist>
    ) : PlaylistsScreenState
}