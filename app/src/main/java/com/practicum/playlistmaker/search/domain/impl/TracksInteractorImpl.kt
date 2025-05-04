package com.practicum.playlistmaker.search.domain.impl

import com.practicum.playlistmaker.common.domain.Resource
import com.practicum.playlistmaker.search.domain.TracksInteractor
import com.practicum.playlistmaker.search.domain.TracksRepository
import java.util.concurrent.Executors

class TracksInteractorImpl(
    private val repository: TracksRepository
) : TracksInteractor {

    private val executor = Executors.newCachedThreadPool()

    override fun searchTracks(expression: String, consumer: TracksInteractor.TracksConsumer) {
        executor.execute {
            when(val resource = repository.searchTracks(expression)) {
                is Resource.Success -> consumer.onSuccess(resource.data!!)
                is Resource.Error -> consumer.onFailure()
            }
        }
    }
}