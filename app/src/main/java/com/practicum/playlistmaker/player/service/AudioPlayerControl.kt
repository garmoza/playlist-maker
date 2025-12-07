package com.practicum.playlistmaker.player.service

import kotlinx.coroutines.flow.StateFlow

interface AudioPlayerControl {
    fun getPlayerState(): StateFlow<PlayerState>
    fun startPlayer()
    fun pausePlayer()
    fun stopPlayer()
    fun startForeground()
    fun stopForeground()
}