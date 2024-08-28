package com.practicum.playlistmaker.clean.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.dpToPx
import com.practicum.playlistmaker.clean.domain.models.Track

class TrackViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.list_item_track, parent, false)
) {

    private val label: ImageView = itemView.findViewById(R.id.label)
    private val title: TextView = itemView.findViewById(R.id.title)
    private val artistName: TextView = itemView.findViewById(R.id.artistName)
    private val trackTime: TextView = itemView.findViewById(R.id.trackTime)

    fun bind(model: Track) {
        Glide.with(itemView)
            .load(model.artworkUrl100)
            .placeholder(R.drawable.placeholder_track_label)
            .centerCrop()
            .transform(RoundedCorners(dpToPx(2F, itemView.context)))
            .into(label)
        title.text = model.trackName ?: UNKNOWN_TRACK_NAME
        artistName.text = model.artistName ?: UNKNOWN_ARTIST_NAME
        trackTime.text = model.trackTime ?: UNKNOWN_TIME
    }

    companion object {
        const val UNKNOWN_TRACK_NAME = "Track Unknown"
        const val UNKNOWN_ARTIST_NAME = "Artist Unknown"
        const val UNKNOWN_TIME = "--:--"
    }
}