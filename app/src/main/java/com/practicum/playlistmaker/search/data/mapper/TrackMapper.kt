package com.practicum.playlistmaker.search.data.mapper

import com.practicum.playlistmaker.search.data.dto.ITunseTrackDto
import com.practicum.playlistmaker.common.domain.models.Track

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