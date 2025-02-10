package com.practicum.playlistmaker.data.mapper

import com.practicum.playlistmaker.data.dto.ITunseTrackDto
import com.practicum.playlistmaker.domain.models.Track

object TrackMapper {
    fun map(dto: ITunseTrackDto) =
        Track(
            trackId = dto.trackId,
            trackName = dto.trackName,
            artistName = dto.artistName,
            trackTimeMillis = dto.trackTimeMillis,
            artworkUrl100 = dto.artworkUrl100,
            collectionName = dto.collectionName,
            releaseDate = dto.releaseDate,
            primaryGenreName = dto.primaryGenreName,
            country = dto.country,
            previewUrl = dto.previewUrl
        )
}