package com.practicum.playlistmaker.search.data

import com.practicum.playlistmaker.common.data.network.NetworkClient
import com.practicum.playlistmaker.search.data.dto.ITunseSearchRequest
import com.practicum.playlistmaker.search.data.dto.ITunseTracksResponse
import com.practicum.playlistmaker.search.data.mapper.TrackMapper
import com.practicum.playlistmaker.common.domain.exceptions.BadResponseException
import com.practicum.playlistmaker.search.domain.api.TracksRepository
import com.practicum.playlistmaker.common.domain.models.Track

class TracksRepositoryImpl(
    private val networkClient: NetworkClient
) : TracksRepository {

    override fun searchTracks(expression: String): List<Track> {
        val response = networkClient.doRequest(ITunseSearchRequest(expression))
        return if (response.resultCode == 200) {
            (response as ITunseTracksResponse).results.map(TrackMapper::map)
        } else {
            throw BadResponseException()
        }
    }
}