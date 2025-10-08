package com.practicum.playlistmaker.playlist.domain

import com.practicum.playlistmaker.common.domain.models.Playlist
import com.practicum.playlistmaker.common.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistInteractor {
    suspend fun addPlaylist(playlist: Playlist)

    fun getPlaylists(): Flow<List<Playlist>>

    suspend fun addTrackToPlaylist(playlist: Playlist, track: Track)
}