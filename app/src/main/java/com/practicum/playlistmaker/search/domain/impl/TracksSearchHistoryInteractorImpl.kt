package com.practicum.playlistmaker.search.domain.impl

import com.practicum.playlistmaker.search.domain.TracksSearchHistoryInteractor
import com.practicum.playlistmaker.search.domain.TracksSearchHistoryRepository
import com.practicum.playlistmaker.common.domain.models.Track

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
}