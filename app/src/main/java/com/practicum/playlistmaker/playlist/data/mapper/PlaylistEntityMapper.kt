package com.practicum.playlistmaker.playlist.data.mapper

import androidx.core.net.toUri
import com.google.gson.Gson
import com.practicum.playlistmaker.common.domain.models.Playlist
import com.practicum.playlistmaker.playlist.data.db.entity.Playlist as PlaylistEntity

class PlaylistEntityMapper(
    private val gson: Gson
) {
    fun map(model: Playlist): PlaylistEntity =
        PlaylistEntity(
            id = model.id,
            name = model.name,
            description = model.description,
            labelUri = model.label?.toString(),
            trackIds = gson.toJson(model.trackIds)
        )

    fun map(entity: PlaylistEntity): Playlist =
        Playlist(
            id = entity.id,
            name = entity.name,
            description = entity.description,
            label = entity.labelUri?.toUri(),
            trackIds = gson.fromJson(entity.trackIds, Array<String>::class.java).toSet()
        )
}