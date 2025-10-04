package com.practicum.playlistmaker.library.data

import com.practicum.playlistmaker.common.domain.models.Playlist
import com.practicum.playlistmaker.library.data.db.dao.PlaylistDao
import com.practicum.playlistmaker.library.data.mapper.PlaylistEntityMapper
import com.practicum.playlistmaker.library.domain.PlaylistRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class PlaylistRepositoryImpl(
    private val playlistDao: PlaylistDao
) : PlaylistRepository {
    override suspend fun addPlaylist(playlist: Playlist) {
        playlistDao.insertPlaylist(
            PlaylistEntityMapper.map(playlist)
        )
    }

    override fun getPlaylists(): Flow<List<Playlist>> = flow {
        val playlists = playlistDao
            .getPlaylists()
            .map(PlaylistEntityMapper::map)
        emit(playlists)
    }.flowOn(Dispatchers.IO)
}