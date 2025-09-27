package com.practicum.playlistmaker.favourite.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.favourite.domain.FavouriteTracksInteractor
import com.practicum.playlistmaker.favourite.domain.model.FavoritesTracksScreenState
import kotlinx.coroutines.launch

class FavoritesTracksViewModel(
    private val favoriteTracksInteractor: FavouriteTracksInteractor
) : ViewModel() {

    private val liveData = MutableLiveData<FavoritesTracksScreenState>(
        FavoritesTracksScreenState.Loading
    )

    fun getLiveData(): LiveData<FavoritesTracksScreenState> = liveData

    fun getFavouriteTracks() {
        liveData.postValue(FavoritesTracksScreenState.Loading)
        viewModelScope.launch {
            favoriteTracksInteractor
                .getFavouriteTracks()
                .collect { tracks ->
                    val state = FavoritesTracksScreenState.Content(tracks)
                    liveData.postValue(state)
                }
        }
    }
}