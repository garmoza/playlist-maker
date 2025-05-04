package com.practicum.playlistmaker.search.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.common.domain.models.Track
import com.practicum.playlistmaker.search.domain.TracksInteractor
import com.practicum.playlistmaker.search.domain.TracksSearchHistoryInteractor
import com.practicum.playlistmaker.search.domain.model.ErrorType
import com.practicum.playlistmaker.search.domain.model.SearchScreenState

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
        tracksInteractor.searchTracks(
            expression = expression,
            consumer = object : TracksInteractor.TracksConsumer {
                override fun onSuccess(foundTracks: List<Track>) {
                    val state = if (foundTracks.isNotEmpty()) {
                        SearchScreenState.Content(foundTracks)
                    } else {
                        SearchScreenState.Error(ErrorType.TRACK_NOT_FOUND)
                    }
                    searchScreenLiveData.postValue(state)
                }

                override fun onFailure() {
                    searchScreenLiveData.postValue(SearchScreenState.Error(ErrorType.NETWORK_PROBLEM))
                }
            }
        )
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