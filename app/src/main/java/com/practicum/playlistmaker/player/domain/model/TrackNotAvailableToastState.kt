package com.practicum.playlistmaker.player.domain.model

sealed interface TrackNotAvailableToastState {
    data object None: TrackNotAvailableToastState
    data object Show: TrackNotAvailableToastState
}