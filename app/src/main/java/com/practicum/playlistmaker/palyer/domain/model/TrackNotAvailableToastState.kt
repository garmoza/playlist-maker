package com.practicum.playlistmaker.palyer.domain.model

sealed interface TrackNotAvailableToastState {
    data object None: TrackNotAvailableToastState
    data object Show: TrackNotAvailableToastState
}