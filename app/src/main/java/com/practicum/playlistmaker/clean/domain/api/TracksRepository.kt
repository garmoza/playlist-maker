package com.practicum.playlistmaker.clean.domain.api

import com.practicum.playlistmaker.clean.domain.models.Track

interface TracksRepository {
    fun searchTracks(expression: String): List<Track>
}