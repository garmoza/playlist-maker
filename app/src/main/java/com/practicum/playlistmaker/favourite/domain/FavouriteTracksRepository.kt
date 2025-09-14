package com.practicum.playlistmaker.favourite.domain

import com.practicum.playlistmaker.common.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface FavouriteTracksRepository {
    suspend fun addFavouriteTrack(track: Track)

    suspend fun removeFavouriteTrack(track: Track)

    fun getFavouriteTracks(): Flow<List<Track>>

    suspend fun existsById(id: String): Boolean
}