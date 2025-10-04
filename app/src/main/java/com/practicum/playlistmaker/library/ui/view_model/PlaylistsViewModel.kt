package com.practicum.playlistmaker.library.ui.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.library.domain.PlaylistInteractor
import com.practicum.playlistmaker.library.domain.model.PlaylistsScreenState
import kotlinx.coroutines.launch

class PlaylistsViewModel(
    private val playlistInteractor: PlaylistInteractor
) : ViewModel() {

    private val liveData = MutableLiveData<PlaylistsScreenState>(
        PlaylistsScreenState.Content(emptyList())
    )

    fun getLiveData(): LiveData<PlaylistsScreenState> = liveData

    init {
        Log.i("PlaylistsViewModel", "Init class")
        viewModelScope.launch {
            playlistInteractor
                .getPlaylists()
                .collect { playlists ->
                    Log.i("PlaylistsViewModel", "Saved playlists: $playlists")
                    val state = PlaylistsScreenState.Content(playlists)
                    liveData.postValue(state)
                }
            Log.i("PlaylistsViewModel", "Complete launch")
        }
        Log.i("PlaylistsViewModel", "Complete init")
    }
}