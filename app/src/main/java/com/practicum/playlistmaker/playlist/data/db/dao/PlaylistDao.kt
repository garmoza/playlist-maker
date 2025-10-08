package com.practicum.playlistmaker.playlist.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.practicum.playlistmaker.playlist.data.db.entity.Playlist
import com.practicum.playlistmaker.playlist.data.db.entity.Track

@Dao
interface PlaylistDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylist(playlist: Playlist)

    @Query("SELECT * FROM playlist")
    suspend fun getPlaylists(): List<Playlist>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTrack(track: Track)
}