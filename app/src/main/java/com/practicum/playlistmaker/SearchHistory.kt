package com.practicum.playlistmaker

import android.content.SharedPreferences
import com.google.gson.Gson
import com.practicum.playlistmaker.model.Track

class SearchHistory(
    private val sharedPreferences: SharedPreferences
) {

    private val historyTracks: ArrayList<Track> = ArrayList()

    init {
        val historyTracksJson = sharedPreferences.getString(HISTORY_TRACKS_KEY, null)
        if (historyTracksJson != null) {
            historyTracks.addAll(createTracksListFromJson(historyTracksJson))
        }
    }

    fun addTrack(track: Track) {
        if (historyTracks.contains(track)) {
            historyTracks.remove(track)
        }
        historyTracks.add(0, track)
        if (historyTracks.size > 10) {
            historyTracks.removeLast()
        }

        updateSharedPreferences()
    }

    fun getTrack(position: Int): Track {
        return historyTracks[position]
    }

    fun getSize(): Int {
        return historyTracks.size
    }

    private fun updateSharedPreferences() {
        sharedPreferences.edit()
            .putString(HISTORY_TRACKS_KEY, createJsonFromTracksList(historyTracks.toTypedArray()))
            .apply()
    }

    private fun createTracksListFromJson(json: String): Array<Track> {
        return Gson().fromJson(json, Array<Track>::class.java)
    }

    private fun createJsonFromTracksList(tracks: Array<Track>): String {
        return Gson().toJson(tracks)
    }
}