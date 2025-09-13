package com.practicum.playlistmaker.favourite.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.favourite.domain.model.FavoritesTracksScreenState

class FavoritesTracksViewModel : ViewModel() {

    private val liveData = MutableLiveData<FavoritesTracksScreenState>(
        FavoritesTracksScreenState.Content(emptyList())
    )

    fun getLiveData(): LiveData<FavoritesTracksScreenState> = liveData
}