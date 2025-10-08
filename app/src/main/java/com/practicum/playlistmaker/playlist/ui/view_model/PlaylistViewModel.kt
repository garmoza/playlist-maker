package com.practicum.playlistmaker.playlist.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.playlist.domain.PlaylistInteractor
import com.practicum.playlistmaker.playlist.domain.model.PlaylistScreenState
import kotlinx.coroutines.launch

class PlaylistViewModel(
    private val playlistInteractor: PlaylistInteractor,
    private val playlistId: Long
) : ViewModel() {

    private val screenLiveData = MutableLiveData<PlaylistScreenState>(
        PlaylistScreenState.Loading
    )

    fun getScreenLiveData(): LiveData<PlaylistScreenState> = screenLiveData

    init {
        viewModelScope.launch {
            val playlist = playlistInteractor.getPlaylistWithTracks(playlistId)
            screenLiveData.postValue(
                PlaylistScreenState.Content(playlist)
            )
        }
    }
}