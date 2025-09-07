package com.practicum.playlistmaker.favourite.domain

import com.practicum.playlistmaker.common.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface FavouriteTracksInteractor {
    fun addFavouriteTrack(track: Track)

    fun removeFavouriteTrack(track: Track)

    fun getFavouriteTracks(): Flow<List<Track>>
}