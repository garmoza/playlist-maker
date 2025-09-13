package com.practicum.playlistmaker.favourite.data.mapper

import com.practicum.playlistmaker.common.domain.models.Track
import com.practicum.playlistmaker.favourite.data.db.entity.FavouriteTrack

object FavouriteTrackEntityMapper {
    fun map(model: Track): FavouriteTrack =
        FavouriteTrack(
            id = model.trackId,
            trackName = model.trackName,
            artistName = model.artistName,
            trackTimeMillis = model.trackTimeMillis,
            artworkUrl100 = model.artworkUrl100,
            collectionName = model.collectionName,
            releaseDate = model.releaseDate,
            primaryGenreName = model.primaryGenreName,
            country = model.country,
            previewUrl = model.previewUrl
        )

    fun map(entity: FavouriteTrack): Track =
        Track(
            trackId = entity.id,
            trackName = entity.trackName,
            artistName = entity.artistName,
            trackTimeMillis = entity.trackTimeMillis,
            artworkUrl100 = entity.artworkUrl100,
            collectionName = entity.collectionName,
            releaseDate = entity.releaseDate,
            primaryGenreName = entity.primaryGenreName,
            country = entity.country,
            previewUrl = entity.previewUrl
        ).apply {
            isFavorite = true
        }
}