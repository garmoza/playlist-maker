package com.practicum.playlistmaker.favourite.domain.model

import com.practicum.playlistmaker.common.domain.models.Track

sealed interface FavoritesTracksScreenState {
    data class Content(
        val tracks: List<Track>
    ) : FavoritesTracksScreenState
}