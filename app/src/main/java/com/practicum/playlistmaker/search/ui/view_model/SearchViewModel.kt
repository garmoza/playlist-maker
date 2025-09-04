package com.practicum.playlistmaker.search.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.common.domain.models.Track
import com.practicum.playlistmaker.search.domain.TracksInteractor
import com.practicum.playlistmaker.search.domain.TracksSearchHistoryInteractor
import com.practicum.playlistmaker.search.domain.model.ErrorType
import com.practicum.playlistmaker.search.domain.model.SearchScreenState
import kotlinx.coroutines.launch

class SearchViewModel(
    private val tracksInteractor: TracksInteractor,
    private val tracksSearchHistoryIneractor: TracksSearchHistoryInteractor
) : ViewModel() {

    private val searchScreenLiveData = MutableLiveData<SearchScreenState>(
        SearchScreenState.Content(emptyList())
    )

    fun getSearchScreenLiveData(): LiveData<SearchScreenState> = searchScreenLiveData

    fun searchTracks(expression: String) {
        searchScreenLiveData.postValue(SearchScreenState.Loading)
        viewModelScope.launch {
            tracksInteractor
                .searchTracks(expression)
                .collect { (foundTracks, error) ->
                    val state = when {
                        error != null -> SearchScreenState.Error(ErrorType.NETWORK_PROBLEM)
                        foundTracks == null -> SearchScreenState.Error(ErrorType.NETWORK_PROBLEM)
                        foundTracks.isEmpty() -> SearchScreenState.Error(ErrorType.TRACK_NOT_FOUND)
                        else -> SearchScreenState.Content(foundTracks)
                    }
                    searchScreenLiveData.postValue(state)
                }
        }
    }

    fun displayHistory() {
        searchScreenLiveData.value = SearchScreenState.History(
            tracksSearchHistoryIneractor.getTracks()
        )
    }

    fun clearHistory() {
        tracksSearchHistoryIneractor.clear()
        searchScreenLiveData.value = SearchScreenState.History(emptyList())
    }

    fun addTrackToHistory(track: Track) {
        tracksSearchHistoryIneractor.addTrack(track)
    }
}