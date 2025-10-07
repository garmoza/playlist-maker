package com.practicum.playlistmaker.library.domain.impl

import android.net.Uri
import com.practicum.playlistmaker.common.domain.PrivateStorageRepository
import com.practicum.playlistmaker.common.domain.models.Playlist
import com.practicum.playlistmaker.common.domain.models.Track
import com.practicum.playlistmaker.library.domain.PlaylistInteractor
import com.practicum.playlistmaker.library.domain.PlaylistRepository
import kotlinx.coroutines.flow.Flow

class PlaylistInteractorImpl(
    private val playlistRepository: PlaylistRepository,
    private val privateStorageRepository: PrivateStorageRepository
) : PlaylistInteractor {
    override suspend fun addPlaylist(playlist: Playlist) {
        playlistRepository.addPlaylist(playlist)
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

    override fun saveLabelToPrivateStorage(uri: Uri, playlistName: String): Uri {
        return privateStorageRepository.savePlaylistLabel(uri, playlistName)
    }
}