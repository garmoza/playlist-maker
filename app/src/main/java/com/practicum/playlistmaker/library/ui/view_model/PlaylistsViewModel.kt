package com.practicum.playlistmaker.library.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.library.domain.model.PlaylistsScreenState

class PlaylistsViewModel : ViewModel() {

    private val liveData = MutableLiveData<PlaylistsScreenState>(
        PlaylistsScreenState.Content(emptyList())
    )

    fun getLiveData(): LiveData<PlaylistsScreenState> = liveData
}