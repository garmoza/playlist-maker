package com.practicum.playlistmaker.player.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MusicService : Service(), AudioPlayerControl {

    private companion object {
        private const val LOG_TAG = "MusicService"
    }

    private val binder = MusicServiceBinder()

    private val _playerState = MutableStateFlow<PlayerState>(PlayerState.Default())
    private val playerState = _playerState.asStateFlow()

    override fun onBind(intent: Intent?): IBinder {
        Log.d(LOG_TAG, "onBind() call")
        return binder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        Log.d(LOG_TAG, "onUnbind() call")
        return super.onUnbind(intent)
    }

    override fun getPlayerState(): StateFlow<PlayerState> {
        return playerState
    }

    override fun startPlayer() {
        TODO("Not yet implemented")
    }

    override fun pausePlayer() {
        TODO("Not yet implemented")
    }

    inner class MusicServiceBinder : Binder() {
        fun getService(): MusicService = this@MusicService
    }
}