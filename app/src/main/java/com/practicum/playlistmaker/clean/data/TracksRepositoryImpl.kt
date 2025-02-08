package com.practicum.playlistmaker.clean.data

import com.practicum.playlistmaker.clean.data.dto.ITunseSearchRequest
import com.practicum.playlistmaker.clean.data.dto.ITunseTracksResponse
import com.practicum.playlistmaker.clean.data.mapper.TrackMapper
import com.practicum.playlistmaker.clean.domain.exceptions.BadResponseException
import com.practicum.playlistmaker.clean.domain.api.TracksRepository
import com.practicum.playlistmaker.clean.domain.models.Track

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