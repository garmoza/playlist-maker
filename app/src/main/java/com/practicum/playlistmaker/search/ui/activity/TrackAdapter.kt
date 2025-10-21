package com.practicum.playlistmaker.search.ui.activity

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.common.domain.models.Track

class TrackAdapter(
    private val onItemClick: (Track) -> Unit,
    private val onLongItemClick: ((Track) -> Unit)? = null
) : RecyclerView.Adapter<TrackViewHolder>() {
    private var items: List<Track> = emptyList()

    fun setItems(newItems: List<Track>) {
        items = newItems
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        return TrackViewHolder(parent)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val track = items[position]
        holder.bind(track)

        holder.itemView.setOnClickListener {
            onItemClick(track)
        }

        holder.itemView.setOnLongClickListener {
            onLongItemClick?.invoke(track)
            onLongItemClick != null
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}