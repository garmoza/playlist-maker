package com.practicum.playlistmaker.favourite.domain.impl

import com.practicum.playlistmaker.common.domain.models.Track
import com.practicum.playlistmaker.favourite.domain.FavouriteTracksInteractor
import com.practicum.playlistmaker.favourite.domain.FavouriteTracksRepository
import kotlinx.coroutines.flow.Flow

class FavouriteTracksInteractorImpl(
    private val favouriteTracksRepository: FavouriteTracksRepository
) : FavouriteTracksInteractor {
    override fun addFavouriteTrack(track: Track) {
        favouriteTracksRepository.addFavouriteTrack(track)
    }

    override fun removeFavouriteTrack(track: Track) {
        favouriteTracksRepository.removeFavouriteTrack(track)
    }

    override fun getFavouriteTracks(): Flow<List<Track>> {
        return favouriteTracksRepository.getFavouriteTracks()
    }
}