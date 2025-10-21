package com.practicum.playlistmaker.playlist.ui.view_model

import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.common.domain.models.Playlist
import com.practicum.playlistmaker.playlist.domain.PlaylistInteractor
import kotlinx.coroutines.launch

class EditPlaylistViewModel(
    private val playlistInteractor: PlaylistInteractor
) : AddPlaylistViewModel(playlistInteractor) {
    private var playlist: Playlist? = null

    fun setPlaylist(playlist: Playlist) {
        this.playlist = playlist
    }

    fun savePlaylist() {
        if (liveData.value?.isReadyToAdd == true) {
            val currentPlaylist = playlist ?: return

            viewModelScope.launch {
                playlistInteractor.addPlaylist(
                    currentPlaylist.copy(
                        name = liveData.value?.playlistName!!,
                        description = liveData.value?.playlistDescription,
                        label = liveData.value?.playlistLabelUri
                    )
                )
            }
        }
    }
}