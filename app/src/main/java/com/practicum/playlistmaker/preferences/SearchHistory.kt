package com.practicum.playlistmaker.preferences

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
        if (historyTracks.size > HISTORY_LIST_SIZE) {
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

    fun clear() {
        historyTracks.clear()

        updateSharedPreferences()
    }

    private fun updateSharedPreferences() {
        sharedPreferences.edit()
            .putString(HISTORY_TRACKS_KEY, createJsonFromTracksList(historyTracks))
            .apply()
    }

    private fun createTracksListFromJson(json: String): List<Track> {
        return Gson().fromJson(json, Array<Track>::class.java).toList()
    }

    private fun createJsonFromTracksList(tracks: List<Track>): String {
        return Gson().toJson(tracks)
    }

    companion object {
        private const val HISTORY_LIST_SIZE = 10
    }
}