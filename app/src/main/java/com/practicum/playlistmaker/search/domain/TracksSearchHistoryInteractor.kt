package com.practicum.playlistmaker.search.domain

import com.practicum.playlistmaker.common.domain.models.Track

interface TracksSearchHistoryInteractor {
    fun addTrack(track: Track)

    fun getTracks(): List<Track>

    fun clear()
}