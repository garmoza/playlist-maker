package com.practicum.playlistmaker.common.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.practicum.playlistmaker.favourite.data.db.dao.FavouriteTrackDao
import com.practicum.playlistmaker.favourite.data.db.entity.FavouriteTrack
import com.practicum.playlistmaker.library.data.db.dao.PlaylistDao
import com.practicum.playlistmaker.library.data.db.entity.Playlist
import com.practicum.playlistmaker.library.data.db.entity.Track

@Database(
    version = 1,
    entities = [
        FavouriteTrack::class,
        Playlist::class,
        Track::class
    ]
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favouriteTrackDao(): FavouriteTrackDao

    abstract fun playlistDao(): PlaylistDao
}