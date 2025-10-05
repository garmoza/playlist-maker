package com.practicum.playlistmaker.library.domain.model

import android.net.Uri

data class AddPlaylistState(
    val playlistName: String?,
    val playlistDescription: String?,
    val playlistLabelUri: Uri?
) {
    val isReadyToAdd: Boolean
        get() = !playlistName.isNullOrBlank()
                && playlistDescription != null
                && playlistLabelUri != null

    val isStartedFilling: Boolean
        get() = !playlistName.isNullOrBlank()
                || !playlistDescription.isNullOrBlank()
                || playlistLabelUri != null
}
