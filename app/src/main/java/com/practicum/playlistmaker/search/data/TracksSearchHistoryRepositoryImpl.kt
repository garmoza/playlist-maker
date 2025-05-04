package com.practicum.playlistmaker.search.data

import android.content.SharedPreferences
import com.google.gson.Gson
import com.practicum.playlistmaker.common.data.preferences.HISTORY_TRACKS_KEY
import com.practicum.playlistmaker.search.domain.TracksSearchHistoryRepository
import com.practicum.playlistmaker.common.domain.models.Track

class TracksSearchHistoryRepositoryImpl(
    private val sharedPreferences: SharedPreferences,
    private val gson: Gson
): TracksSearchHistoryRepository {

    private val historyTracks: MutableList<Track> = mutableListOf()

    init {
        val historyTracksJson = sharedPreferences.getString(HISTORY_TRACKS_KEY, null)
        if (historyTracksJson != null) {
            historyTracks.addAll(createTracksListFromJson(historyTracksJson))
        }
    }

    override fun addTrack(track: Track) {
        if (historyTracks.contains(track)) {
            historyTracks.remove(track)
        }
        historyTracks.add(0, track)
        if (historyTracks.size > HISTORY_LIST_SIZE) {
            historyTracks.removeLast()
        }

        updateSharedPreferences()
    }

    override fun getSize(): Int {
        return historyTracks.size
    }

    override fun clear() {
        historyTracks.clear()

        updateSharedPreferences()
    }

    override fun getTracks(): List<Track> =
        historyTracks.toList()

    private fun updateSharedPreferences() {
        sharedPreferences.edit()
            .putString(HISTORY_TRACKS_KEY, createJsonFromTracksList(historyTracks))
            .apply()
    }

    private fun createTracksListFromJson(json: String): List<Track> {
        return gson.fromJson(json, Array<Track>::class.java).toList()
    }

    private fun createJsonFromTracksList(tracks: List<Track>): String {
        return gson.toJson(tracks)
    }

    companion object {
        private const val HISTORY_LIST_SIZE = 10
    }
}