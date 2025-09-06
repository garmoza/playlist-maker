package com.practicum.playlistmaker.favourite.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.practicum.playlistmaker.favourite.data.db.entity.FavouriteTrack

@Dao
interface FavouriteTrackDao {

    @Insert
    fun insertFavouriteTrack(favouriteTrack: FavouriteTrack)

    @Delete
    fun deleteFavouriteTrack(favouriteTrack: FavouriteTrack)

    @Query("SELECT * FROM favourite_track")
    fun getFavouriteTracks(): List<FavouriteTrack>

    @Query("SELECT id FROM favourite_track")
    fun getFavouriteTrackIds(): List<String>
}