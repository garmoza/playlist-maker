package com.practicum.playlistmaker.palyer.ui.activity

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.common.domain.models.Playlist
import com.practicum.playlistmaker.common.ui.dpToPx

class PlaylistBottomSheetViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.list_item_playlist_for_bottom_sheet, parent, false)
) {

    private val label: ImageView = itemView.findViewById(R.id.label)
    private val playlistName: TextView = itemView.findViewById(R.id.playlistName)
    private val tracksCount: TextView = itemView.findViewById(R.id.tracksCount)

    fun bind(model: Playlist) {
        Glide.with(itemView)
            .load(model.label)
            .placeholder(R.drawable.placeholder_track_label)
            .transform(CenterCrop(),  RoundedCorners(dpToPx(2F, itemView.context)))
            .into(label)
        playlistName.text = model.name

        val numberOfTracks = model.trackIds.size
        tracksCount.text = itemView.resources.getQuantityString(
            R.plurals.number_of_tracks,
            numberOfTracks,
            numberOfTracks
        )
    }
}