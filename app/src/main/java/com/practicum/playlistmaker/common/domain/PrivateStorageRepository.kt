package com.practicum.playlistmaker.common.domain

import android.net.Uri

interface PrivateStorageRepository {
    fun savePlaylistLabel(uri: Uri, playlistName: String): Uri
}