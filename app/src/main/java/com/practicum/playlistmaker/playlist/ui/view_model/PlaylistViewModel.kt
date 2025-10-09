package com.practicum.playlistmaker.playlist.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.common.domain.models.Playlist
import com.practicum.playlistmaker.common.domain.models.Track
import com.practicum.playlistmaker.playlist.domain.PlaylistInteractor
import com.practicum.playlistmaker.playlist.domain.model.PlaylistScreenState
import com.practicum.playlistmaker.playlist.domain.model.PlaylistToastState
import com.practicum.playlistmaker.sharing.domain.SharingInteractor
import kotlinx.coroutines.launch

class PlaylistViewModel(
    private val playlistInteractor: PlaylistInteractor,
    private val sharingInteractor: SharingInteractor,
    private val playlistId: Long
) : ViewModel() {

    private val screenLiveData = MutableLiveData<PlaylistScreenState>(
        PlaylistScreenState.Loading
    )
    private val toastLiveData = MutableLiveData<PlaylistToastState>(
        PlaylistToastState.None
    )

    fun getScreenLiveData(): LiveData<PlaylistScreenState> = screenLiveData
    fun getToastLiveData(): LiveData<PlaylistToastState> = toastLiveData

    init {
        loadContent()
    }

    fun loadContent() {
        viewModelScope.launch {
            val playlist = playlistInteractor.getPlaylistWithTracks(playlistId)
            screenLiveData.postValue(
                PlaylistScreenState.Content(playlist)
            )
        }
    }

    fun deleteTrackFromPlaylist(track: Track) {
        viewModelScope.launch {
            val playlist = (screenLiveData.value as PlaylistScreenState.Content)
                .playlistWithTracks.playlist
            playlistInteractor.deleteTrackFromPlaylist(
                playlist = playlist,
                track = track
            )
            loadContent()
        }
    }

    fun sharePlaylist() {
        val tracks = (screenLiveData.value as PlaylistScreenState.Content)
            .playlistWithTracks.tracks
        if (tracks.isEmpty()) {
            toastLiveData.value = PlaylistToastState.CantShareEmptyPlaylist
        } else {
            sharingInteractor.sharePlaylist(
                (screenLiveData.value as PlaylistScreenState.Content).playlistWithTracks
            )
        }
    }

    fun toastWasShow() {
        toastLiveData.value = PlaylistToastState.None
    }

    fun deletePlaylist() {
        viewModelScope.launch {
            playlistInteractor.deletePlaylistWithTracks(
                (screenLiveData.value as PlaylistScreenState.Content).playlistWithTracks
            )
        }
    }

    fun getPlaylist(): Playlist =
        (screenLiveData.value as PlaylistScreenState.Content).playlistWithTracks.playlist
}