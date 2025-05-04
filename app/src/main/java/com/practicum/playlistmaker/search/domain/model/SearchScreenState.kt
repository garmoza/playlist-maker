package com.practicum.playlistmaker.search.domain.model

import com.practicum.playlistmaker.common.domain.models.Track

sealed interface SearchScreenState {
    data object Loading : SearchScreenState
    data class Content(
        val tracks: List<Track>
    ) : SearchScreenState
    data class History(
        val tracks: List<Track>
    ) : SearchScreenState
    data class Error(
        val type: ErrorType
    ) : SearchScreenState
}