package com.practicum.playlistmaker.search.domain

import com.practicum.playlistmaker.common.domain.models.Track

interface TracksInteractor {
    fun searchTracks(expression: String, consumer: TracksConsumer)

    interface TracksConsumer {
        fun onSuccess(foundTracks: List<Track>)

        fun onFailure()
    }
}