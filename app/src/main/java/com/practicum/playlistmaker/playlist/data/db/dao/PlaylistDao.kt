package com.practicum.playlistmaker.playlist.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.practicum.playlistmaker.playlist.data.db.entity.Playlist
import com.practicum.playlistmaker.playlist.data.db.entity.Track

@Dao
interface PlaylistDao {

    @Upsert
    suspend fun upsertPlaylist(playlist: Playlist)

    @Query("SELECT * FROM playlist")
    suspend fun getPlaylists(): List<Playlist>

    @Query("SELECT * FROM playlist WHERE id = :playlistId")
    suspend fun getPlaylistById(playlistId: Long): Playlist

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTrack(track: Track)

    @Query("SELECT * FROM track")
    suspend fun getTracks(): List<Track>

    @Delete
    suspend fun deleteTrack(track: Track)
}