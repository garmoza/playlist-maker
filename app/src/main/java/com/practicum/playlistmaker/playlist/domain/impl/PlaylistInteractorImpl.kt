package com.practicum.playlistmaker.playlist.domain.impl

import com.practicum.playlistmaker.common.domain.PrivateStorageRepository
import com.practicum.playlistmaker.common.domain.models.Playlist
import com.practicum.playlistmaker.common.domain.models.Track
import com.practicum.playlistmaker.playlist.domain.PlaylistInteractor
import com.practicum.playlistmaker.playlist.domain.PlaylistRepository
import kotlinx.coroutines.flow.Flow

class PlaylistInteractorImpl(
    private val playlistRepository: PlaylistRepository,
    private val privateStorageRepository: PrivateStorageRepository
) : PlaylistInteractor {
    override suspend fun addPlaylist(playlist: Playlist) {
        val storageUri = playlist.label?.let {
            privateStorageRepository.savePlaylistLabel(playlist.label, playlist.name)
        }
        playlistRepository.addPlaylist(
            playlist.copy(label = storageUri)
        )
    }

    override fun getPlaylists(): Flow<List<Playlist>> {
        return playlistRepository.getPlaylists()
    }

    override suspend fun addTrackToPlaylist(playlist: Playlist, track: Track) {
        val newTrackIds = playlist.trackIds.toMutableSet().apply {
            add(track.trackId)
        }
        playlistRepository.addTrack(track)
        playlistRepository.addPlaylist(playlist.copy(trackIds = newTrackIds))
    }
}