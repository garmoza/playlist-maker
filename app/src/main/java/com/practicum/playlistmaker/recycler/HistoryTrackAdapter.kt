package com.practicum.playlistmaker.recycler

import android.content.Intent
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.activity.PlayerActivity
import com.practicum.playlistmaker.preferences.SearchHistory

class HistoryTrackAdapter(
    private val searchHistory: SearchHistory
) : RecyclerView.Adapter<TrackViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        return TrackViewHolder(parent)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(searchHistory.getTrack(position))

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val displayIntent = Intent(context, PlayerActivity::class.java)
            context.startActivity(displayIntent)
        }
    }

    override fun getItemCount(): Int {
        return searchHistory.getSize()
    }
}