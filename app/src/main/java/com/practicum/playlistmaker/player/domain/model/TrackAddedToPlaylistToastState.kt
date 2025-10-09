package com.practicum.playlistmaker.player.domain.model

sealed interface TrackAddedToPlaylistToastState {
    data class ShowNewAdded(val trackName: String): TrackAddedToPlaylistToastState
    data class ShowAlreadyAdded(val trackName: String): TrackAddedToPlaylistToastState
    data object None: TrackAddedToPlaylistToastState
}