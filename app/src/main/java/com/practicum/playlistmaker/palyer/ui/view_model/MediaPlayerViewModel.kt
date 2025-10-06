package com.practicum.playlistmaker.palyer.ui.view_model

import android.media.MediaPlayer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.common.domain.models.Playlist
import com.practicum.playlistmaker.common.domain.models.Track
import com.practicum.playlistmaker.favourite.domain.FavouriteTracksInteractor
import com.practicum.playlistmaker.library.domain.PlaylistInteractor
import com.practicum.playlistmaker.palyer.domain.model.PlayerState
import com.practicum.playlistmaker.palyer.domain.model.TrackAddedToPlaylistToastState
import com.practicum.playlistmaker.palyer.domain.model.TrackNotAvailableToastState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MediaPlayerViewModel(
    private val favouriteTracksInteractor: FavouriteTracksInteractor,
    private val playlistInteractor: PlaylistInteractor,
    private val track: Track
) : ViewModel() {

    private var mediaPlayer: MediaPlayer = MediaPlayer()

    private var timerJob: Job? = null

    private val playerLiveData = MutableLiveData(LOADING_STATE)
    private val trackNotAvailableToastLiveData = MutableLiveData<TrackNotAvailableToastState>(
        TrackNotAvailableToastState.None
    )
    private val playlistsLiveData = MutableLiveData(emptyList<Playlist>())
    private val trackAddedToPlaylistToastLiveData = MutableLiveData<TrackAddedToPlaylistToastState>(
        TrackAddedToPlaylistToastState.None
    )

    init {
        viewModelScope.launch {
            val isFavourite = favouriteTracksInteractor.existsById(track.trackId)
            playerLiveData.value = playerLiveData.value?.copy(
                isLoading = false,
                isFavourite = isFavourite
            )
        }

        prepareMediaPlayer()

        loadPlaylists()
    }

    private fun prepareMediaPlayer() {
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
    fun getPlaylistsLiveData(): LiveData<List<Playlist>> = playlistsLiveData
    fun getTrackAddedToPlaylistLiveData(): LiveData<TrackAddedToPlaylistToastState> = trackAddedToPlaylistToastLiveData

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

                loadPlaylists()
            }
        }
    }

    companion object {
        private val LOADING_STATE = PlayerState(
            isLoading = true,
            isTrackAvailable = false,
            isPlaying = false,
            isFavourite = false,
            progress = 0
        )

        private const val DELAY = 300L

        private const val UNKNOWN_TRACK_NAME = "Track Unknown"
    }
}