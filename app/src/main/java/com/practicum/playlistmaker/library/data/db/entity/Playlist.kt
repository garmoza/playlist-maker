package com.practicum.playlistmaker.library.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlist")
data class Playlist(
    @PrimaryKey(autoGenerate = true)
    val id: Long?,
    val name: String,
    val description: String?,
    val labelUri: String?,
    val trackIds: String = "",
    val numberOfTracks: Int = 0
)