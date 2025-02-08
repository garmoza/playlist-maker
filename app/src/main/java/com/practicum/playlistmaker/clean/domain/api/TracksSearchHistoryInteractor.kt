package com.practicum.playlistmaker.clean.domain.api

import com.practicum.playlistmaker.clean.domain.models.Track

interface TracksSearchHistoryInteractor {
    fun addTrack(track: Track)

    fun getTracks(): List<Track>

    fun clear()

    fun registerOnTrackSearchHistoryChangeListener(consumer: () -> Unit)
}