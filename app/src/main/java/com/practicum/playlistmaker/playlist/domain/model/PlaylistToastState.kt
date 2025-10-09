package com.practicum.playlistmaker.playlist.domain.model

sealed interface PlaylistToastState {
    data object None : PlaylistToastState
    data object CantShareEmptyPlaylist :PlaylistToastState
}