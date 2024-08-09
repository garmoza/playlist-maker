package com.practicum.playlistmaker.recycler

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.model.Track
import com.practicum.playlistmaker.preferences.SearchHistory

class HistoryTrackAdapter(
    private val searchHistory: SearchHistory,
    private val onItemClick: (Track) -> Unit
) : RecyclerView.Adapter<TrackViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        return TrackViewHolder(parent)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val track = searchHistory.getTrack(position)
        holder.bind(track)

        holder.itemView.setOnClickListener {
            onItemClick(track)
        }
    }

    override fun getItemCount(): Int {
        return searchHistory.getSize()
    }
}