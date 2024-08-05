package com.practicum.playlistmaker.recycler

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.preferences.SearchHistory

class HistoryTrackAdapter(
    private val searchHistory: SearchHistory
) : RecyclerView.Adapter<TrackViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        return TrackViewHolder(parent)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(searchHistory.getTrack(position))
    }

    override fun getItemCount(): Int {
        return searchHistory.getSize()
    }
}