package com.practicum.playlistmaker.clean.domain.api

import com.practicum.playlistmaker.clean.domain.models.Track

interface TracksSearchHistoryRepository {
    fun addTrack(track: Track)

    fun getSize(): Int

    fun clear()

    fun getTracks(): List<Track>
}