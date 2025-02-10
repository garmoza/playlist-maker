package com.practicum.playlistmaker.domain.impl

import com.practicum.playlistmaker.domain.api.TracksSearchHistoryInteractor
import com.practicum.playlistmaker.domain.api.TracksSearchHistoryRepository
import com.practicum.playlistmaker.domain.models.Track

class TracksSearchHistoryInteractorImpl(
    private val repository: TracksSearchHistoryRepository
) : TracksSearchHistoryInteractor {
    override fun addTrack(track: Track) {
        repository.addTrack(track)
    }

    override fun getTracks(): List<Track> =
        repository.getTracks()

    override fun clear() {
        repository.clear()
    }

    override fun registerOnTrackSearchHistoryChangeListener(consumer: () -> Unit) {
        repository.registerOnTrackSearchHistoryChangeListener(consumer)
    }
}