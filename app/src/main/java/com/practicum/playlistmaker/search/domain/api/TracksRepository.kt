package com.practicum.playlistmaker.search.domain.api

import com.practicum.playlistmaker.common.domain.Resource
import com.practicum.playlistmaker.common.domain.models.Track

interface TracksRepository {
    fun searchTracks(expression: String): Resource<List<Track>>
}