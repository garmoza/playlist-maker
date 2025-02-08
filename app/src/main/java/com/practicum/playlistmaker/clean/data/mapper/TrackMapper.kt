package com.practicum.playlistmaker.clean.data.mapper

import com.practicum.playlistmaker.clean.data.dto.ITunseTrackDto
import com.practicum.playlistmaker.clean.domain.models.Track

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