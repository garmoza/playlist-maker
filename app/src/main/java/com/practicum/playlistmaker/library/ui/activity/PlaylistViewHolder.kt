package com.practicum.playlistmaker.library.ui.activity

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.common.domain.models.Playlist
import androidx.core.net.toUri
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.common.ui.dpToPx

class PlaylistViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.list_item_playlist_for_grid, parent, false)
) {

    private val label: ImageView = itemView.findViewById(R.id.label)
    private val playlistName: TextView = itemView.findViewById(R.id.playlistName)
    private val tracksCount: TextView = itemView.findViewById(R.id.tracksCount)

    fun bind(model: Playlist) {
        Glide.with(itemView)
            .load(model.labelUri?.toUri())
            .placeholder(R.drawable.placeholder_track_label)
            .transform(CenterCrop(),  RoundedCorners(dpToPx(8F, itemView.context)))
            .into(label)
        playlistName.text = model.name
        tracksCount.text = "0 tracks"
    }
}