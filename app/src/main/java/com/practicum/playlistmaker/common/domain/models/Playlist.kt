package com.practicum.playlistmaker.common.domain.models

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Playlist(
    val id: Long? = null,
    val name: String,
    val description: String?,
    val label: Uri?,
    val trackIds: List<String> = emptyList()
) : Parcelable
