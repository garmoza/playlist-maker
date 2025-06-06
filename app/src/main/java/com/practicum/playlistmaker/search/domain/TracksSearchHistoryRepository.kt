package com.practicum.playlistmaker.search.domain

import com.practicum.playlistmaker.common.domain.models.Track

interface TracksSearchHistoryRepository {
    fun addTrack(track: Track)

    fun getSize(): Int

    fun clear()

    fun getTracks(): List<Track>
}