package com.practicum.playlistmaker.common.domain.models

data class Playlist(
    val id: Long? = null,
    val name: String,
    val description: String,
    val labelUri: String
)
