package com.practicum.playlistmaker.player.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import com.practicum.playlistmaker.R
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
        const val TRACK_URL_EXTRA_NAME = "TRACK_URL_EXTRA_NAME"

        private const val LOG_TAG = "MusicService"
        private const val DELAY = 300L
        private const val NOTIFICATION_CHANNEL_ID = "music_service_channel"
        private const val SERVICE_NOTIFICATION_ID = 100
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

        createNotificationChannel()

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
            ServiceCompat.stopForeground(this, ServiceCompat.STOP_FOREGROUND_REMOVE)
        }
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            "Music service",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        channel.description = "Service for playing music"

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    private fun createServiceNotification(): Notification {
        return NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setContentTitle("Music foreground service")
            .setContentText("Our service is working right now!")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setCategory(NotificationCompat.CATEGORY_SERVICE)
            .build()
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

        ServiceCompat.startForeground(
            this,
            SERVICE_NOTIFICATION_ID,
            createServiceNotification(),
            ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK
        )
    }

    override fun pausePlayer() {
        timerJob?.cancel()

        mediaPlayer.pause()
        _playerState.value = PlayerState.Paused(mediaPlayer.currentPosition)

        ServiceCompat.stopForeground(this, ServiceCompat.STOP_FOREGROUND_REMOVE)
    }

    inner class MusicServiceBinder : Binder() {
        fun getService(): MusicService = this@MusicService
    }
}