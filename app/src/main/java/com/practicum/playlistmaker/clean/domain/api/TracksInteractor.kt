package com.practicum.playlistmaker.clean.domain.api

import com.practicum.playlistmaker.clean.domain.models.Track

interface TracksInteractor {
    fun searchTracks(expression: String, consumer: TracksConsumer)

    interface TracksConsumer {
        fun consume(foundTracks: List<Track>)
    }
}