package com.practicum.playlistmaker.library.ui.activity

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.common.domain.models.Playlist
import androidx.core.net.toUri
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.common.ui.dpToPx

class PlaylistViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.list_item_playlist, parent, false)
) {

    private val context: Context = parent.context
    private val label: ImageView = itemView.findViewById(R.id.label)

    fun bind(model: Playlist) {
        Glide.with(itemView)
            .load(model.labelUri.toUri())
            .transform(CenterCrop(),  RoundedCorners(dpToPx(8F, context)))
            .placeholder(R.drawable.placeholder_track_label)
            .into(label)
    }
}