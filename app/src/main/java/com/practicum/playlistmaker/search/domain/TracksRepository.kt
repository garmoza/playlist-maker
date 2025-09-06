package com.practicum.playlistmaker.search.domain

import com.practicum.playlistmaker.common.domain.Resource
import com.practicum.playlistmaker.common.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface TracksRepository {
    fun searchTracks(expression: String): Flow<Resource<List<Track>>>
}