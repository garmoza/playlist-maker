package com.practicum.playlistmaker.search.domain

import com.practicum.playlistmaker.common.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface TracksSearchHistoryInteractor {
    fun addTrack(track: Track)

    fun getTracks(): Flow<List<Track>>

    fun clear()
}