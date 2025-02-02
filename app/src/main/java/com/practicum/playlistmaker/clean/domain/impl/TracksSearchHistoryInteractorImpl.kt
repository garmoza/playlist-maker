package com.practicum.playlistmaker.clean.domain.impl

import com.practicum.playlistmaker.clean.domain.api.TracksSearchHistoryInteractor
import com.practicum.playlistmaker.clean.domain.api.TracksSearchHistoryRepository
import com.practicum.playlistmaker.clean.domain.models.Track

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