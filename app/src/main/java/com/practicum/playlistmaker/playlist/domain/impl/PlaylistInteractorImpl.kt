package com.practicum.playlistmaker.playlist.domain.impl

import com.practicum.playlistmaker.common.domain.PrivateStorageRepository
import com.practicum.playlistmaker.common.domain.models.Playlist
import com.practicum.playlistmaker.common.domain.models.PlaylistWithTracks
import com.practicum.playlistmaker.common.domain.models.Track
import com.practicum.playlistmaker.playlist.domain.PlaylistInteractor
import com.practicum.playlistmaker.playlist.domain.PlaylistRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class PlaylistInteractorImpl(
    private val playlistRepository: PlaylistRepository,
    private val privateStorageRepository: PrivateStorageRepository
) : PlaylistInteractor {
    override suspend fun addPlaylist(playlist: Playlist) {
        val storageUri = playlist.label?.let {
            privateStorageRepository.savePlaylistLabel(playlist.label, playlist.name)
        }
        playlistRepository.savePlaylist(
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
        playlistRepository.savePlaylist(playlist.copy(trackIds = newTrackIds))
    }

    override suspend fun getPlaylistWithTracks(playlistId: Long): PlaylistWithTracks {
        val playlist = playlistRepository.getPlaylistById(playlistId)
        val tracks = playlistRepository.getTracks(playlist.trackIds).first()
        return PlaylistWithTracks(playlist, tracks)
    }

    override suspend fun deleteTrackFromPlaylist(playlist: Playlist, track: Track) {
        val newTrackIds = playlist.trackIds.toMutableSet().apply {
            remove(track.trackId)
        }
        playlistRepository.savePlaylist(playlist.copy(trackIds = newTrackIds))

        val usedTrackIds = getAllPlaylistTrackIds()
        if (!usedTrackIds.contains(track.trackId)) {
            playlistRepository.removeTrack(track)
        }
    }

    private suspend fun getAllPlaylistTrackIds(): Set<String> =
        playlistRepository.getPlaylists()
            .first().flatMap { it.trackIds }.toSet()

    override suspend fun deletePlaylistWithTracks(playlistWithTracks: PlaylistWithTracks) {
        playlistRepository.removePlaylist(playlistWithTracks.playlist)

        val usedTrackIds = getAllPlaylistTrackIds()
        playlistWithTracks.tracks.forEach { track ->
            if (!usedTrackIds.contains(track.trackId)) {
                playlistRepository.removeTrack(track)
            }
        }
    }
}