package com.practicum.playlistmaker.palyer.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.playlistmaker.palyer.domain.model.TrackNotAvailableToastState
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.palyer.domain.Player
import com.practicum.playlistmaker.palyer.domain.model.PlayerStatus

class PlayerViewModel(
    trackUrl: String?,
    private val player: Player
) : ViewModel() {

    companion object {
        fun getViewModelFactory(trackUrl: String?): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                PlayerViewModel(
                    trackUrl,
                    Creator.providePlayer()
                )
            }
        }
        private val DEFAULT_PLAYER_STATUS = PlayerStatus(
            isTrackAvailable = false,
            isPlaying = false,
            progress = 0
        )
    }

    private val playerStatusLiveData = MutableLiveData<PlayerStatus>()
    private val trackNotAvailableToastState = MutableLiveData<TrackNotAvailableToastState>(
        TrackNotAvailableToastState.None
    )

    init {
        trackUrl?.let {
            player.prepare(it, object : Player.StatusObserver {
                override fun onPrepared() {
                    playerStatusLiveData.value = DEFAULT_PLAYER_STATUS.copy(isTrackAvailable = true)
                }

                override fun onPlay() {
                    playerStatusLiveData.value = playerStatusLiveData.value?.copy(isPlaying = true)
                }

                override fun onPause() {
                    playerStatusLiveData.value = playerStatusLiveData.value?.copy(isPlaying = false)
                }

                override fun onProgress(progress: Int) {
                    playerStatusLiveData.value = playerStatusLiveData.value?.copy(progress = progress)
                }
            })
        }
    }

    fun getPlayerStatusLiveData(): LiveData<PlayerStatus> = playerStatusLiveData
    fun getToastState(): LiveData<TrackNotAvailableToastState> = trackNotAvailableToastState

    fun switchBetweenPlayAndPause() {
        if (playerStatusLiveData.value?.isTrackAvailable == true) {
            player.switchBetweenPlayAndPause()
        } else {
            trackNotAvailableToastState.value = TrackNotAvailableToastState.Show
        }
    }

    fun pause() {
        if (playerStatusLiveData.value?.isTrackAvailable == true) {
            player.pause()
        } else {
            trackNotAvailableToastState.value = TrackNotAvailableToastState.Show
        }
    }

    override fun onCleared() {
        player.release()
    }

    fun toastWasShow() {
        trackNotAvailableToastState.value = TrackNotAvailableToastState.None
    }
}