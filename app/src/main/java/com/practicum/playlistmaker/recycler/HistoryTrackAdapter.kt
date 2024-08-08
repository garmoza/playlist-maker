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
        val track = searchHistory.getTrack(position)
        holder.bind(track)

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val displayIntent = Intent(context, PlayerActivity::class.java)
            displayIntent.putExtra("artworkUrl512", track.artworkUrl512)
            displayIntent.putExtra("trackName", track.trackName)
            displayIntent.putExtra("artistName", track.artistName)
            displayIntent.putExtra("collectionName", track.collectionName)
            displayIntent.putExtra("releaseYear", track.releaseYear)
            displayIntent.putExtra("primaryGenreName", track.primaryGenreName)
            displayIntent.putExtra("country", track.country)
            displayIntent.putExtra("trackTime", track.trackTime)
            context.startActivity(displayIntent)
        }
    }

    override fun getItemCount(): Int {
        return searchHistory.getSize()
    }
}