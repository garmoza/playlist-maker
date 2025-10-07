package com.practicum.playlistmaker.favourite.data

import com.practicum.playlistmaker.common.domain.models.Track
import com.practicum.playlistmaker.favourite.data.db.dao.FavouriteTrackDao
import com.practicum.playlistmaker.favourite.data.mapper.FavouriteTrackEntityMapper
import com.practicum.playlistmaker.favourite.domain.FavouriteTracksRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavouriteTracksRepositoryImpl(
    private val favouriteTrackDao: FavouriteTrackDao
) : FavouriteTracksRepository {

    override suspend fun addFavouriteTrack(track: Track) {
        favouriteTrackDao.insertFavouriteTrack(
            FavouriteTrackEntityMapper.map(track)
        )
    }

    override suspend fun removeFavouriteTrack(track: Track) {
        favouriteTrackDao.deleteFavouriteTrack(
            FavouriteTrackEntityMapper.map(track)
        )
    }

    override fun getFavouriteTracks(): Flow<List<Track>> = flow {
        val tracks = favouriteTrackDao
            .getFavouriteTracks()
            .map { entity -> FavouriteTrackEntityMapper.map(entity) }
            .reversed()
        emit(tracks)
    }

    override suspend fun existsById(id: String): Boolean {
        return favouriteTrackDao.existsById(id)
    }
}