package com.practicum.playlistmaker

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.model.Track

class TrackAdapter(
    private val items: ArrayList<Track>,
    private val searchHistory: SearchHistory
) : RecyclerView.Adapter<TrackViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        return TrackViewHolder(parent)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val track = items[position]
        holder.bind(track)

        holder.itemView.setOnClickListener {
            searchHistory.addTrack(track)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}