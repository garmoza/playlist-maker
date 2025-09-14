package com.practicum.playlistmaker.palyer.domain.model

data class PlayerState(
    val isLoading: Boolean,
    val isTrackAvailable: Boolean,
    val isPlaying: Boolean,
    val isFavourite: Boolean,
    val progress: Int
)