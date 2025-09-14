package com.practicum.playlistmaker.favourite.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.practicum.playlistmaker.favourite.data.db.entity.FavouriteTrack

@Dao
interface FavouriteTrackDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavouriteTrack(favouriteTrack: FavouriteTrack)

    @Delete
    suspend fun deleteFavouriteTrack(favouriteTrack: FavouriteTrack)

    @Query("SELECT * FROM favourite_track")
    suspend fun getFavouriteTracks(): List<FavouriteTrack>

    @Query("SELECT id FROM favourite_track")
    suspend fun getFavouriteTrackIds(): List<String>

    @Query("SELECT EXISTS(SELECT 1 FROM favourite_track WHERE id = :id)")
    suspend fun existsById(id: String): Boolean
}