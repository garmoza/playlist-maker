package com.practicum.playlistmaker.palyer.ui.activity

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.common.domain.models.Playlist

class PlaylistBottomSheetAdapter : RecyclerView.Adapter<PlaylistBottomSheetViewHolder>() {
    private var items: List<Playlist> = emptyList()

    fun setItems(newItems: List<Playlist>) {
        items = newItems
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PlaylistBottomSheetViewHolder {
        return PlaylistBottomSheetViewHolder(parent)
    }

    override fun onBindViewHolder(holder: PlaylistBottomSheetViewHolder, position: Int) {
        val playlist = items[position]
        holder.bind(playlist)
    }

    override fun getItemCount(): Int {
        return items.size
    }
}