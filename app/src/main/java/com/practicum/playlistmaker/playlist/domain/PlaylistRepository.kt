package com.practicum.playlistmaker.playlist.domain

import com.practicum.playlistmaker.common.domain.models.Playlist
import com.practicum.playlistmaker.common.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {
    suspend fun savePlaylist(playlist: Playlist)

    fun getPlaylists(): Flow<List<Playlist>>

    suspend fun getPlaylistById(playlistId: Long): Playlist

    suspend fun removePlaylist(playlist: Playlist)

    suspend fun addTrack(track: Track)

    fun getTracks(trackIds: List<String>): Flow<List<Track>>

    suspend fun removeTrack(track: Track)
}