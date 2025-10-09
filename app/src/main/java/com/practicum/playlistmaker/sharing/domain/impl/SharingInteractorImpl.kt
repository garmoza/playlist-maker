package com.practicum.playlistmaker.sharing.domain.impl

import android.content.res.Resources
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.common.domain.models.PlaylistWithTracks
import com.practicum.playlistmaker.sharing.domain.ExternalNavigator
import com.practicum.playlistmaker.sharing.domain.SharingInteractor
import com.practicum.playlistmaker.sharing.domain.model.EmailData

class SharingInteractorImpl(
    private val externalNavigator: ExternalNavigator,
    private val resources: Resources
) : SharingInteractor {
    override fun shareApp(link: String) {
        externalNavigator.shareText(link)
    }

    override fun openTerms(link: String) {
        externalNavigator.openLink(link)
    }

    override fun openSupport(emailData: EmailData) {
        externalNavigator.openEmail(emailData)
    }

    override fun sharePlaylist(playlistWithTracks: PlaylistWithTracks) {
        val playlistName = playlistWithTracks.playlist.name
        val playlistDescription = playlistWithTracks.playlist.description ?: EMPTY_PLAYLIST_DESCRIPTION
        val numberOfTracks = resources.getQuantityString(
            R.plurals.number_of_tracks,
            playlistWithTracks.tracks.size,
            playlistWithTracks.tracks.size
        )
        val listOfTracks = mutableListOf<String>()
        playlistWithTracks.tracks.forEachIndexed { index, track ->
            val artistName = track.artistName ?: UNKNOWN_ARTIST_NAME
            val trackName = track.trackName ?: UNKNOWN_TRACK_NAME
            val trackTime = track.trackTime ?: UNKNOWN_TIME
            listOfTracks += "${index + 1}. $artistName - $trackName ($trackTime)"
        }

        val text = listOf(
            playlistName,
            playlistDescription,
            numberOfTracks,
            *(listOfTracks.toTypedArray())
        ).joinToString(separator = "\n")
        externalNavigator.shareText(text)
    }

    companion object {
        const val EMPTY_PLAYLIST_DESCRIPTION = ""
        const val UNKNOWN_TRACK_NAME = "Track Unknown"
        const val UNKNOWN_ARTIST_NAME = "Artist Unknown"
        const val UNKNOWN_TIME = "--:--"
    }
}