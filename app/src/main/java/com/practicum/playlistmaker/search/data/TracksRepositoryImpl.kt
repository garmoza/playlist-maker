package com.practicum.playlistmaker.search.data

import com.practicum.playlistmaker.common.domain.Resource
import com.practicum.playlistmaker.common.data.network.NetworkClient
import com.practicum.playlistmaker.search.data.dto.ITunseSearchRequest
import com.practicum.playlistmaker.search.data.dto.ITunseTracksResponse
import com.practicum.playlistmaker.search.data.mapper.TrackMapper
import com.practicum.playlistmaker.search.domain.TracksRepository
import com.practicum.playlistmaker.common.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TracksRepositoryImpl(
    private val networkClient: NetworkClient,
) : TracksRepository {

    override fun searchTracks(expression: String): Flow<Resource<List<Track>>> = flow {
        val response = networkClient.doRequest(ITunseSearchRequest(expression))
        when (response.resultCode) {
            -1 -> {
                emit(Resource.Error("No internet access"))
            }
            200 -> {
                with(response as ITunseTracksResponse) {
                    val tracks = results.map(TrackMapper::map)
                    emit(Resource.Success(tracks))
                }
            }
            else -> {
                emit(Resource.Error("Server error"))
            }
        }
    }
}