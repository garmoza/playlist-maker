package com.practicum.playlistmaker.palyer.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.palyer.domain.model.TrackNotAvailableToastState
import com.practicum.playlistmaker.palyer.domain.Player
import com.practicum.playlistmaker.palyer.domain.model.PlayerState

class PlayerViewModel(
    trackUrl: String?,
    private val player: Player
) : ViewModel() {

    private val playerLiveData = MutableLiveData<PlayerState>()
    private val trackNotAvailableToastLiveData = MutableLiveData<TrackNotAvailableToastState>(
        TrackNotAvailableToastState.None
    )

    init {
        trackUrl?.let {
            player.prepare(it, object : Player.StatusObserver {
                override fun onPrepared() {
                    playerLiveData.value = DEFAULT_PLAYER_STATE.copy(isTrackAvailable = true)
                }

                override fun onPlay() {
                    playerLiveData.value = playerLiveData.value?.copy(isPlaying = true)
                }

                override fun onPause() {
                    playerLiveData.value = playerLiveData.value?.copy(isPlaying = false)
                }

                override fun onProgress(progress: Int) {
                    playerLiveData.value = playerLiveData.value?.copy(progress = progress)
                }
            })
        }
    }

    fun getPlayerLiveData(): LiveData<PlayerState> = playerLiveData
    fun getToastLiveData(): LiveData<TrackNotAvailableToastState> = trackNotAvailableToastLiveData

    fun switchBetweenPlayAndPause() {
        if (playerLiveData.value?.isTrackAvailable == true) {
            player.switchBetweenPlayAndPause()
        } else {
            trackNotAvailableToastLiveData.value = TrackNotAvailableToastState.Show
        }
    }

    fun pause() {
        if (playerLiveData.value?.isTrackAvailable == true) {
            player.pause()
        } else {
            trackNotAvailableToastLiveData.value = TrackNotAvailableToastState.Show
        }
    }

    override fun onCleared() {
        player.release()
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
    }
}