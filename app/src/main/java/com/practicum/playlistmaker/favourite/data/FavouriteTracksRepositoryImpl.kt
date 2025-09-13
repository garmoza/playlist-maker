package com.practicum.playlistmaker.favourite.data

import com.practicum.playlistmaker.common.data.db.AppDatabase
import com.practicum.playlistmaker.common.domain.models.Track
import com.practicum.playlistmaker.favourite.data.db.dao.FavouriteTrackDao
import com.practicum.playlistmaker.favourite.data.mapper.FavouriteTrackEntityMapper
import com.practicum.playlistmaker.favourite.domain.FavouriteTracksRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class FavouriteTracksRepositoryImpl(
    appDatabase: AppDatabase
) : FavouriteTracksRepository {

    private val favouriteTrackDao: FavouriteTrackDao = appDatabase.favouriteTrackDao()

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
    }.flowOn(Dispatchers.IO)
}