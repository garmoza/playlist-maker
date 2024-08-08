package com.practicum.playlistmaker.recycler

import android.content.Intent
import android.util.Log
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.activity.PlayerActivity
import com.practicum.playlistmaker.activity.SearchActivity
import com.practicum.playlistmaker.model.Track
import com.practicum.playlistmaker.preferences.SearchHistory

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

            val context = holder.itemView.context
            val displayIntent = Intent(context, PlayerActivity::class.java)
            displayIntent.putExtra("trackName", track.trackName)
            displayIntent.putExtra("artistName", track.artistName)
            displayIntent.putExtra("collectionName", track.collectionName)
            displayIntent.putExtra("releaseDate", track.releaseDate)
            displayIntent.putExtra("primaryGenreName", track.primaryGenreName)
            context.startActivity(displayIntent)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}