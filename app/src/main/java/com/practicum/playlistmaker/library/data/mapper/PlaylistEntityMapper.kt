package com.practicum.playlistmaker.library.data.mapper

import com.google.gson.Gson
import com.practicum.playlistmaker.common.domain.models.Playlist
import com.practicum.playlistmaker.library.data.db.entity.Playlist as PlaylistEntity

class PlaylistEntityMapper(
    private val gson: Gson
) {
    fun map(model: Playlist): PlaylistEntity =
        PlaylistEntity(
            id = model.id,
            name = model.name,
            description = model.description,
            labelUri = model.labelUri,
            trackIds = gson.toJson(model.trackIds)
        )

    fun map(entity: PlaylistEntity): Playlist =
        Playlist(
            id = entity.id,
            name = entity.name,
            description = entity.description,
            labelUri = entity.labelUri,
            trackIds = gson.fromJson(entity.trackIds, Array<Long>::class.java).toSet()
        )
}