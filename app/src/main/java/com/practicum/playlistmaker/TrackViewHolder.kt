package com.practicum.playlistmaker

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

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
        title.text = model.trackName
        artistName.text = model.artistName
        trackTime.text = model.trackTime
    }
}