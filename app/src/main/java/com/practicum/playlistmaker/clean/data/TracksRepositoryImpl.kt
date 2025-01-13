package com.practicum.playlistmaker.clean.data

import com.practicum.playlistmaker.clean.data.dto.ITunseSearchRequest
import com.practicum.playlistmaker.clean.data.dto.ITunseTracksResponse
import com.practicum.playlistmaker.clean.data.network.BadResponseException
import com.practicum.playlistmaker.clean.domain.api.TracksRepository
import com.practicum.playlistmaker.clean.domain.models.Track

class TracksRepositoryImpl(
    private val networkClient: NetworkClient
) : TracksRepository {

    override fun searchTracks(expression: String): List<Track> {
        val response = networkClient.doRequest(ITunseSearchRequest(expression))
        return if (response.resultCode == 200) {
            (response as ITunseTracksResponse).results.map {
                Track(
                    trackId = it.trackId,
                    trackName = it.trackName,
                    artistName = it.artistName,
                    trackTimeMillis = it.trackTimeMillis,
                    artworkUrl100 = it.artworkUrl100,
                    collectionName = it.collectionName,
                    releaseDate = it.releaseDate,
                    primaryGenreName = it.primaryGenreName,
                    country = it.country,
                    previewUrl = it.previewUrl
                )
            }
        } else {
            throw BadResponseException()
        }
    }
}