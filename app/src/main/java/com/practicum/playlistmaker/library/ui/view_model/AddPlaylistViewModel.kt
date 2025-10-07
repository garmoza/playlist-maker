package com.practicum.playlistmaker.library.ui.view_model

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.common.domain.models.Playlist
import com.practicum.playlistmaker.library.domain.PlaylistInteractor
import com.practicum.playlistmaker.library.domain.model.AddPlaylistState
import kotlinx.coroutines.launch

class AddPlaylistViewModel(
    private val playlistInteractor: PlaylistInteractor
) : ViewModel() {

    private val liveData = MutableLiveData(EMPTY_STATE)

    fun getLiveData(): LiveData<AddPlaylistState> = liveData

    fun addPlaylist() {
        if (liveData.value?.isReadyToAdd == true) {
            viewModelScope.launch {
                playlistInteractor.addPlaylist(
                    Playlist(
                        name = liveData.value?.playlistName!!,
                        description = liveData.value?.playlistDescription,
                        label = liveData.value?.playlistLabelUri
                    )
                )
            }
        }
    }

    fun onPickPlaylistLabel(uri: Uri) {
        liveData.value = liveData.value?.copy(playlistLabelUri = uri)
    }

    fun onNameChanged(name: String) {
        liveData.value = liveData.value?.copy(playlistName = name)
    }

    fun onDescriptionChanged(description: String) {
        liveData.value = liveData.value?.copy(playlistDescription = description)
    }

    companion object {
        private val EMPTY_STATE = AddPlaylistState(
            playlistName = null,
            playlistDescription = null,
            playlistLabelUri = null
        )
    }
}