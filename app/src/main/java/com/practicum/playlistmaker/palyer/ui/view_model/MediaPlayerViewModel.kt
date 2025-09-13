package com.practicum.playlistmaker.palyer.ui.view_model

import android.media.MediaPlayer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.common.domain.models.Track
import com.practicum.playlistmaker.favourite.domain.FavouriteTracksInteractor
import com.practicum.playlistmaker.palyer.domain.model.PlayerState
import com.practicum.playlistmaker.palyer.domain.model.TrackNotAvailableToastState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MediaPlayerViewModel(
    private val favouriteTracksInteractor: FavouriteTracksInteractor,
    private val track: Track
) : ViewModel() {

    private var mediaPlayer: MediaPlayer = MediaPlayer()

    private var timerJob: Job? = null

    private val playerLiveData = MutableLiveData(DEFAULT_PLAYER_STATE.copy(isFavourite = track.isFavorite))
    private val trackNotAvailableToastLiveData = MutableLiveData<TrackNotAvailableToastState>(
        TrackNotAvailableToastState.None
    )

    init {
        mediaPlayer.setDataSource(track.previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerLiveData.value = playerLiveData.value?.copy(isTrackAvailable = true)
        }
        mediaPlayer.setOnCompletionListener {
            playerLiveData.value = playerLiveData.value?.copy(isTrackAvailable = true)
        }
    }

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

    fun onFavouriteClick() {
        viewModelScope.launch {
            if (playerLiveData.value?.isFavourite == true) {
                track.isFavorite = false
                playerLiveData.value = playerLiveData.value?.copy(isFavourite = false)
                favouriteTracksInteractor.removeFavouriteTrack(track)
            } else {
                track.isFavorite = true
                playerLiveData.value = playerLiveData.value?.copy(isFavourite = true)
                favouriteTracksInteractor.addFavouriteTrack(track)
            }
        }
    }

    companion object {
        private val DEFAULT_PLAYER_STATE = PlayerState(
            isTrackAvailable = false,
            isPlaying = false,
            isFavourite = false,
            progress = 0
        )

        private const val DELAY = 300L
    }
}