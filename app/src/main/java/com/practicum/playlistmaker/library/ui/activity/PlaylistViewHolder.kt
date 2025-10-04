package com.practicum.playlistmaker.library.ui.activity

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.common.domain.models.Playlist

class PlaylistViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.list_item_playlist, parent, false)
) {

    private val label: ImageView = itemView.findViewById(R.id.label)

    fun bind(model: Playlist) {
        label.setImageResource(R.drawable.placeholder_track_label)
    }
}