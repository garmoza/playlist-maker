package com.practicum.playlistmaker.palyer.domain.model

data class PlayerStatus(
    val isTrackAvailable: Boolean,
    val isPlaying: Boolean,
    val progress: Int
)