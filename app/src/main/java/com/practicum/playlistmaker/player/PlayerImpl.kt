package com.practicum.playlistmaker.player

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import android.widget.ImageButton
import android.widget.TextView
import com.practicum.playlistmaker.R
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerImpl(
    private val playButton: ImageButton,
    private val playtimeTextView: TextView
) : Player {

    private val mainThreadHandler = Handler(Looper.getMainLooper())
    private val mediaPlayer = MediaPlayer()

    private var playerState = STATE_DEFAULT

    private var updatePlaytimeTask = object : Runnable {
        override fun run() {
            playtimeTextView.text = SimpleDateFormat("mm:ss", Locale.getDefault())
                .format(mediaPlayer.currentPosition)
            mainThreadHandler.postDelayed(this, DELAY)
        }
    }

    override fun prepare(url: String) {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playButton.setImageResource(R.drawable.ic_play_track)
            playtimeTextView.text = DEFAULT_PLAYTIME
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            mainThreadHandler.removeCallbacks(updatePlaytimeTask)

            playButton.setImageResource(R.drawable.ic_play_track)
            playtimeTextView.text = DEFAULT_PLAYTIME
            playerState = STATE_PREPARED
        }
    }

    override fun play() {
        mainThreadHandler.post(updatePlaytimeTask)

        mediaPlayer.start()
        playButton.setImageResource(R.drawable.ic_pause_track)
        playerState = STATE_PLAYING
    }

    override fun pause() {
        mainThreadHandler.removeCallbacks(updatePlaytimeTask)

        mediaPlayer.pause()
        playButton.setImageResource(R.drawable.ic_play_track)
        playerState = STATE_PAUSED
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

        private const val DEFAULT_PLAYTIME = "00:00"
    }
}