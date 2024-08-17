package com.practicum.playlistmaker.model

import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.Locale

data class Track(
    val trackId: String?,
    val trackName: String?,
    val artistName: String?,
    val trackTimeMillis: Long?,
    val artworkUrl100: String?,
    val collectionName: String?,
    val releaseDate: String?,
    val primaryGenreName: String?,
    val country: String?
) : Serializable {
    val trackTime: String?
        get() = trackTimeMillis?.let {
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(it)
        }
    val artworkUrl512: String?
        get() = artworkUrl100?.replaceAfterLast('/', "512x512bb.jpg")
    val releaseYear: String?
        get() = releaseDate?.substring(0, 4)
}
