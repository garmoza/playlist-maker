package com.practicum.playlistmaker.palyer.data

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import com.practicum.playlistmaker.palyer.domain.Player

class PlayerImpl : Player {

    private lateinit var statusObserver: Player.StatusObserver

    private val mainThreadHandler = Handler(Looper.getMainLooper())
    private val mediaPlayer = MediaPlayer()

    private var playerState = STATE_DEFAULT

    private var updatePlaytimeTask = object : Runnable {
        override fun run() {
            statusObserver.onProgress(mediaPlayer.currentPosition)
            mainThreadHandler.postDelayed(this, DELAY)
        }
    }

    override fun prepare(url: String, statusObserver: Player.StatusObserver) {
        this.statusObserver = statusObserver
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            statusObserver.onPrepared()
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            mainThreadHandler.removeCallbacks(updatePlaytimeTask)

            statusObserver.onPrepared()
            playerState = STATE_PREPARED
        }
    }

    override fun play() {
        statusObserver.onPlay()
        mainThreadHandler.post(updatePlaytimeTask)

        mediaPlayer.start()
        playerState = STATE_PLAYING
    }

    override fun pause() {
        mainThreadHandler.removeCallbacks(updatePlaytimeTask)

        mediaPlayer.pause()
        playerState = STATE_PAUSED
        statusObserver.onPause()
    }

    override fun release() {
        mainThreadHandler.removeCallbacks(updatePlaytimeTask)
        mediaPlayer.release()
    }

    override fun switchBetweenPlayAndPause() {
        when(playerState) {
            STATE_PLAYING -> {
                pause()
            }
            STATE_PREPARED, STATE_PAUSED -> {
                play()
            }
        }
    }

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3

        private const val DELAY = 300L
    }
}