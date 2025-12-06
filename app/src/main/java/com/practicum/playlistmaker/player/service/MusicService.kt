package com.practicum.playlistmaker.player.service

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MusicService : Service(), AudioPlayerControl {

    companion object {
        private const val LOG_TAG = "MusicService"

        const val TRACK_URL_EXTRA_NAME = "TRACK_URL_EXTRA_NAME"

        private const val DELAY = 300L
    }

    private var mediaPlayer: MediaPlayer = MediaPlayer()

    private var timerJob: Job? = null

    private val binder = MusicServiceBinder()

    private val _playerState = MutableStateFlow<PlayerState>(PlayerState.Default())
    private val playerState = _playerState.asStateFlow()

    override fun onBind(intent: Intent?): IBinder {
        Log.d(LOG_TAG, "onBind() call")
        val trackUrl = intent?.getStringExtra(TRACK_URL_EXTRA_NAME)
        prepareMediaPlayer(trackUrl)

        return binder
    }

    private fun prepareMediaPlayer(trackUrl: String?) {
        mediaPlayer.setDataSource(trackUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            Log.d(LOG_TAG, "Media Player prepared")
            _playerState.value = PlayerState.Prepared()
        }
        mediaPlayer.setOnCompletionListener {
            Log.d(LOG_TAG, "Playback competed")
            _playerState.value = PlayerState.Prepared()
        }
    }

    private fun updatePlaytime() {
        timerJob?.cancel()
        timerJob = CoroutineScope(Dispatchers.Default).launch {
            while (mediaPlayer.isPlaying) {
                _playerState.value = PlayerState.Playing(mediaPlayer.currentPosition)
                delay(DELAY)
            }
        }
    }

    override fun onUnbind(intent: Intent?): Boolean {
        Log.d(LOG_TAG, "onUnbind() call")
        releasePlayer()
        return super.onUnbind(intent)
    }

    private fun releasePlayer() {
        timerJob?.cancel()
        mediaPlayer.release()
    }

    override fun getPlayerState(): StateFlow<PlayerState> {
        return playerState
    }

    override fun startPlayer() {
        // reset media player progress when needed
        if (playerState.value.progress == 0) {
            mediaPlayer.seekTo(0)
        }

        _playerState.value = PlayerState.Playing(mediaPlayer.currentPosition)

        mediaPlayer.start()
        updatePlaytime()
    }

    override fun pausePlayer() {
        timerJob?.cancel()

        mediaPlayer.pause()
        _playerState.value = PlayerState.Paused(mediaPlayer.currentPosition)
    }

    inner class MusicServiceBinder : Binder() {
        fun getService(): MusicService = this@MusicService
    }
}