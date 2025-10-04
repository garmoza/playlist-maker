package com.practicum.playlistmaker.library.data.mapper

import com.practicum.playlistmaker.common.domain.models.Playlist
import com.practicum.playlistmaker.library.data.db.entity.Playlist as PlaylistEntity

object PlaylistEntityMapper {
    fun map(model: Playlist): PlaylistEntity =
        PlaylistEntity(
            id = model.id,
            name = model.name,
            description = model.description,
            labelPath = model.labelPath
        )

    fun map(entity: PlaylistEntity): Playlist =
        Playlist(
            id = entity.id,
            name = entity.name,
            description = entity.description,
            labelPath = entity.labelPath
        )
}