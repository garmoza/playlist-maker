package com.practicum.playlistmaker.domain.api

import com.practicum.playlistmaker.domain.models.Track

interface TracksSearchHistoryInteractor {
    fun addTrack(track: Track)

    fun getTracks(): List<Track>

    fun clear()

    fun registerOnTrackSearchHistoryChangeListener(consumer: () -> Unit)
}