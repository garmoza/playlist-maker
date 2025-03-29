package com.practicum.playlistmaker.search.data

import com.practicum.playlistmaker.common.domain.Resource
import com.practicum.playlistmaker.common.data.network.NetworkClient
import com.practicum.playlistmaker.search.data.dto.ITunseSearchRequest
import com.practicum.playlistmaker.search.data.dto.ITunseTracksResponse
import com.practicum.playlistmaker.search.data.mapper.TrackMapper
import com.practicum.playlistmaker.search.domain.TracksRepository
import com.practicum.playlistmaker.common.domain.models.Track

class TracksRepositoryImpl(
    private val networkClient: NetworkClient
) : TracksRepository {

    override fun searchTracks(expression: String): Resource<List<Track>> {
        val response = networkClient.doRequest(ITunseSearchRequest(expression))
        return when (response.resultCode) {
            -1 -> {
                Resource.Error("No internet access")
            }
            200 -> Resource.Success(
                (response as ITunseTracksResponse).results.map(TrackMapper::map)
            )
            else -> {
                Resource.Error("Server error")
            }
        }
    }
}