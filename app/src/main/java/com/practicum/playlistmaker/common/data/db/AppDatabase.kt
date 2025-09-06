package com.practicum.playlistmaker.common.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.practicum.playlistmaker.favourite.data.db.dao.FavouriteTrackDao
import com.practicum.playlistmaker.favourite.data.db.entity.FavouriteTrack

@Database(
    version = 1,
    entities = [
        FavouriteTrack::class
    ]
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favouriteTrackDao(): FavouriteTrackDao
}