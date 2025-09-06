package com.practicum.playlistmaker.palyer.ui.view_model

import android.media.MediaPlayer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.palyer.domain.model.PlayerState
import com.practicum.playlistmaker.palyer.domain.model.TrackNotAvailableToastState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MediaPlayerViewModel(
    trackUrl: String?
) : ViewModel() {

    private var mediaPlayer: MediaPlayer = MediaPlayer()

    private var timerJob: Job? = null

    init {
        mediaPlayer.setDataSource(trackUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerLiveData.value = DEFAULT_PLAYER_STATE.copy(isTrackAvailable = true)
        }
        mediaPlayer.setOnCompletionListener {
            playerLiveData.value = DEFAULT_PLAYER_STATE.copy(isTrackAvailable = true)
        }
    }

    private val playerLiveData = MutableLiveData<PlayerState>()
    private val trackNotAvailableToastLiveData = MutableLiveData<TrackNotAvailableToastState>(
        TrackNotAvailableToastState.None
    )

    fun getPlayerLiveData(): LiveData<PlayerState> = playerLiveData
    fun getToastLiveData(): LiveData<TrackNotAvailableToastState> = trackNotAvailableToastLiveData

    fun switchBetweenPlayAndPause() {
        if (playerLiveData.value?.isPlaying == true) {
            pause()
        } else {
            play()
        }
    }

    private fun play() {
        if (playerLiveData.value?.isTrackAvailable == true) {
            playerLiveData.value = playerLiveData.value?.copy(isPlaying = true)

            mediaPlayer.start()
            updatePlaytime()
        } else {
            trackNotAvailableToastLiveData.value = TrackNotAvailableToastState.Show
        }
    }

    fun pause() {
        if (playerLiveData.value?.isTrackAvailable == true) {
            timerJob?.cancel()

            mediaPlayer.pause()
            playerLiveData.value = playerLiveData.value?.copy(isPlaying = false)
        } else {
            trackNotAvailableToastLiveData.value = TrackNotAvailableToastState.Show
        }
    }

    override fun onCleared() {
        super.onCleared()
        mediaPlayer.release()
    }

    private fun updatePlaytime() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (mediaPlayer.isPlaying) {
                playerLiveData.value = playerLiveData.value?.copy(progress = mediaPlayer.currentPosition)
                delay(DELAY)
            }
        }
    }

    fun toastWasShow() {
        trackNotAvailableToastLiveData.value = TrackNotAvailableToastState.None
    }

    companion object {
        private val DEFAULT_PLAYER_STATE = PlayerState(
            isTrackAvailable = false,
            isPlaying = false,
            progress = 0
        )

        private const val DELAY = 300L
    }
}