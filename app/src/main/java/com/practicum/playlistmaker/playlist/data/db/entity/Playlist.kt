package com.practicum.playlistmaker.playlist.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlist")
data class Playlist(
    @PrimaryKey(autoGenerate = true)
    val id: Long?,
    val name: String,
    val description: String?,
    val labelUri: String?,
    val trackIds: String = ""
)