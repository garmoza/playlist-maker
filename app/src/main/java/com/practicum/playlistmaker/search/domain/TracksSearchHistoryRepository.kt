package com.practicum.playlistmaker.search.domain

import com.practicum.playlistmaker.common.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface TracksSearchHistoryRepository {
    fun addTrack(track: Track)

    fun getSize(): Int

    fun clear()

    fun getTracks(): Flow<List<Track>>
}