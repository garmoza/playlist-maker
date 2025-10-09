package com.practicum.playlistmaker.playlist.data

import com.practicum.playlistmaker.common.domain.models.Playlist
import com.practicum.playlistmaker.common.domain.models.Track
import com.practicum.playlistmaker.playlist.data.db.dao.PlaylistDao
import com.practicum.playlistmaker.playlist.data.mapper.PlaylistEntityMapper
import com.practicum.playlistmaker.playlist.data.mapper.TrackEntityMapper
import com.practicum.playlistmaker.playlist.domain.PlaylistRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlaylistRepositoryImpl(
    private val playlistDao: PlaylistDao,
    private val playlistEntityMapper: PlaylistEntityMapper
) : PlaylistRepository {
    override suspend fun savePlaylist(playlist: Playlist) {
        playlistDao.upsertPlaylist(
            playlistEntityMapper.map(playlist)
        )
    }

    override fun getPlaylists(): Flow<List<Playlist>> = flow {
        val playlists = playlistDao
            .getPlaylists()
            .map(playlistEntityMapper::map)
        emit(playlists)
    }

    override suspend fun getPlaylistById(playlistId: Long): Playlist {
        val playlist = playlistDao.getPlaylistById(playlistId)
        return playlistEntityMapper.map(playlist)
    }

    override suspend fun removePlaylist(playlist: Playlist) {
        playlistDao.deletePlaylist(playlistEntityMapper.map(playlist))
    }

    override suspend fun addTrack(track: Track) {
        playlistDao.insertTrack(
            TrackEntityMapper.map(track)
        )
    }

    override fun getTracks(trackIds: Set<String>): Flow<List<Track>> = flow {
        val tracks = playlistDao
            .getTracks()
            .filter { track -> track.id in trackIds }
            .map(TrackEntityMapper::map)
        emit(tracks)
    }

    override suspend fun removeTrack(track: Track) {
        playlistDao.deleteTrack(TrackEntityMapper.map(track))
    }
}