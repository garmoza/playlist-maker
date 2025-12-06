package com.practicum.playlistmaker.player.service

sealed class PlayerState(
    val isTrackAvailable: Boolean,
    val isPlaying: Boolean,
    val progress: Int
) {

    class Default : PlayerState(false, false, 0)
    class Prepared : PlayerState(true, false, 0)
    class Playing(progress: Int) : PlayerState(true, true, progress)
    class Paused(progress: Int) : PlayerState(true, false, progress)
}