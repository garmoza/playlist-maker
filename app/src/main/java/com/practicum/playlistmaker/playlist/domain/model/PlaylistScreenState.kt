package com.practicum.playlistmaker.playlist.domain.model

import com.practicum.playlistmaker.common.domain.models.PlaylistWithTracks

sealed interface PlaylistScreenState {
    data object Loading : PlaylistScreenState
    data class Content(val playlist: PlaylistWithTracks) : PlaylistScreenState
}