package com.practicum.playlistmaker.clean.domain.impl

import com.practicum.playlistmaker.clean.domain.api.TracksInteractor
import com.practicum.playlistmaker.clean.domain.api.TracksRepository
import java.util.concurrent.Executors

class TracksInteractorImpl(
    private val repository: TracksRepository
) : TracksInteractor {

    private val executor = Executors.newCachedThreadPool()

    override fun searchTracks(expression: String, consumer: TracksInteractor.TracksConsumer) {
        executor.execute {
            consumer.consume(repository.searchTracks(expression))
        }
    }
}