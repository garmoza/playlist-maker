package com.practicum.playlistmaker.common.domain.models

import android.net.Uri

data class Playlist(
    val id: Long? = null,
    val name: String,
    val description: String?,
    val label: Uri?,
    val trackIds: Set<String> = emptySet()
)
