package com.practicum.playlistmaker.search.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.playlistmaker.common.domain.models.Track
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.search.domain.TracksInteractor
import com.practicum.playlistmaker.search.domain.TracksSearchHistoryInteractor
import com.practicum.playlistmaker.search.domain.model.ErrorType
import com.practicum.playlistmaker.search.domain.model.SearchScreenState

class SearchViewModel(
    private val tracksInteractor: TracksInteractor,
    private val tracksSearchHistoryIneractor: TracksSearchHistoryInteractor
) :ViewModel() {

    companion object {
        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                SearchViewModel(
                    Creator.provideTracksInteractor(),
                    Creator.provideTracksSearchHistoryInteractor()
                )
            }
        }
    }

    private val screenStateLiveData = MutableLiveData<SearchScreenState>(
        SearchScreenState.Content(emptyList())
    )

    fun getScreenState(): LiveData<SearchScreenState> = screenStateLiveData

    fun searchTracks(expression: String) {
        screenStateLiveData.postValue(SearchScreenState.Loading)
        tracksInteractor.searchTracks(
            expression = expression,
            consumer = object : TracksInteractor.TracksConsumer {
                override fun onSuccess(foundTracks: List<Track>) {
                    val state = if (foundTracks.isNotEmpty()) {
                        SearchScreenState.Content(foundTracks)
                    } else {
                        SearchScreenState.Error(ErrorType.TRACK_NOT_FOUND)
                    }
                    screenStateLiveData.postValue(state)
                }

                override fun onFailure() {
                    screenStateLiveData.postValue(SearchScreenState.Error(ErrorType.NETWORK_PROBLEM))
                }
            }
        )
    }

    fun displayHistory() {
        screenStateLiveData.value = SearchScreenState.History(
            tracksSearchHistoryIneractor.getTracks()
        )
    }

    fun clearHistory() {
        tracksSearchHistoryIneractor.clear()
        screenStateLiveData.value = SearchScreenState.History(emptyList())
    }

    fun addTrackToHistory(track: Track) {
        tracksSearchHistoryIneractor.addTrack(track)
    }
}