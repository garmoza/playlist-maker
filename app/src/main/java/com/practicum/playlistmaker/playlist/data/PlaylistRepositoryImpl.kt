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
    override suspend fun addPlaylist(playlist: Playlist) {
        playlistDao.insertPlaylist(
            playlistEntityMapper.map(playlist)
        )
    }

    override fun getPlaylists(): Flow<List<Playlist>> = flow {
        val playlists = playlistDao
            .getPlaylists()
            .map(playlistEntityMapper::map)
        emit(playlists)
    }

    override suspend fun addTrack(track: Track) {
        playlistDao.insertTrack(
            TrackEntityMapper.map(track)
        )
    }
}