package com.practicum.playlistmaker.playlist.domain.model

import android.net.Uri

data class AddPlaylistState(
    val playlistName: String?,
    val playlistDescription: String?,
    val playlistLabelUri: Uri?
) {
    val isReadyToAdd: Boolean
        get() = !playlistName.isNullOrBlank()

    val isStartedFilling: Boolean
        get() = !playlistName.isNullOrBlank()
                || !playlistDescription.isNullOrBlank()
                || playlistLabelUri != null
}
