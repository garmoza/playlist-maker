package com.practicum.playlistmaker.player.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.common.domain.models.Playlist
import com.practicum.playlistmaker.common.domain.models.Track
import com.practicum.playlistmaker.favourite.domain.FavouriteTracksInteractor
import com.practicum.playlistmaker.playlist.domain.PlaylistInteractor
import com.practicum.playlistmaker.player.domain.model.PlayerScreenState
import com.practicum.playlistmaker.player.domain.model.TrackAddedToPlaylistToastState
import com.practicum.playlistmaker.player.domain.model.TrackNotAvailableToastState
import com.practicum.playlistmaker.player.service.AudioPlayerControl
import kotlinx.coroutines.launch

class MediaPlayerViewModel(
    private val favouriteTracksInteractor: FavouriteTracksInteractor,
    private val playlistInteractor: PlaylistInteractor,
    private val track: Track
) : ViewModel() {

    private val playerLiveData = MutableLiveData(LOADING_STATE)
    private val trackNotAvailableToastLiveData = MutableLiveData<TrackNotAvailableToastState>(
        TrackNotAvailableToastState.None
    )
    private val playlistsLiveData = MutableLiveData(emptyList<Playlist>())
    private val trackAddedToPlaylistToastLiveData = MutableLiveData<TrackAddedToPlaylistToastState>(
        TrackAddedToPlaylistToastState.None
    )

    private var audioPlayerControl: AudioPlayerControl? = null

    init {
        viewModelScope.launch {
            val isFavourite = favouriteTracksInteractor.existsById(track.trackId)
            playerLiveData.value = playerLiveData.value?.copy(
                isLoading = false,
                isFavourite = isFavourite
            )
        }

        loadPlaylists()
    }

    fun getPlayerLiveData(): LiveData<PlayerScreenState> = playerLiveData
    fun getToastLiveData(): LiveData<TrackNotAvailableToastState> = trackNotAvailableToastLiveData
    fun getPlaylistsLiveData(): LiveData<List<Playlist>> = playlistsLiveData
    fun getTrackAddedToPlaylistLiveData(): LiveData<TrackAddedToPlaylistToastState> = trackAddedToPlaylistToastLiveData

    fun setAudioPlayerControl(audioPlayerControl: AudioPlayerControl) {
        this.audioPlayerControl = audioPlayerControl

        viewModelScope.launch {
            audioPlayerControl.getPlayerState().collect {
                playerLiveData.value = playerLiveData.value?.copy(
                    isTrackAvailable = it.isTrackAvailable,
                    isPlaying = it.isPlaying,
                    progress = it.progress
                )
            }
        }
    }

    fun removeAudioPlayerControl() {
        audioPlayerControl = null
    }

    fun switchBetweenPlayAndPause() {
        if (playerLiveData.value?.isPlaying == true) {
            pause()
        } else {
            play()
        }
    }

    private fun play() {
        if (playerLiveData.value?.isTrackAvailable == true) {
            audioPlayerControl?.startPlayer()
        } else {
            trackNotAvailableToastLiveData.value = TrackNotAvailableToastState.Show
        }
    }

    private fun pause() {
        if (playerLiveData.value?.isTrackAvailable == true) {
            audioPlayerControl?.pausePlayer()
        } else {
            trackNotAvailableToastLiveData.value = TrackNotAvailableToastState.Show
        }
    }

    fun toastWasShow() {
        trackNotAvailableToastLiveData.value = TrackNotAvailableToastState.None
        trackAddedToPlaylistToastLiveData.value = TrackAddedToPlaylistToastState.None
    }

    fun onFavouriteClick() {
        viewModelScope.launch {
            if (playerLiveData.value?.isFavourite == true) {
                playerLiveData.value = playerLiveData.value?.copy(isFavourite = false)
                favouriteTracksInteractor.removeFavouriteTrack(track)
            } else {
                playerLiveData.value = playerLiveData.value?.copy(isFavourite = true)
                favouriteTracksInteractor.addFavouriteTrack(track)
            }
        }
    }

    fun loadPlaylists() {
        viewModelScope.launch {
            playlistInteractor
                .getPlaylists()
                .collect { playlists ->
                    playlistsLiveData.postValue(playlists)
                }
        }
    }

    fun addCurrentTrackToPlaylist(playlist: Playlist) {
        val trackName = track.trackName ?: UNKNOWN_TRACK_NAME
        if (playlist.trackIds.contains(track.trackId)) {
            trackAddedToPlaylistToastLiveData.value = TrackAddedToPlaylistToastState.ShowAlreadyAdded(trackName)
        } else {
            viewModelScope.launch {
                playlistInteractor.addTrackToPlaylist(playlist, track)
                trackAddedToPlaylistToastLiveData.value = TrackAddedToPlaylistToastState.ShowNewAdded(trackName)
            }
        }
    }

    companion object {
        private val LOADING_STATE = PlayerScreenState(
            isLoading = true,
            isTrackAvailable = false,
            isPlaying = false,
            isFavourite = false,
            progress = 0
        )

        private const val UNKNOWN_TRACK_NAME = "Track Unknown"
    }
}