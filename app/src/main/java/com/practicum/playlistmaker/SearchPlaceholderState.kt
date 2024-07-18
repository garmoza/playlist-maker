package com.practicum.playlistmaker

import androidx.annotation.IdRes
import androidx.annotation.StringRes

enum class SearchPlaceholderState(
    @IdRes var image: Int? = null,
    @StringRes var message: Int? = null,
    @StringRes var additionalMessage: Int? = null,
    @StringRes var button: Int? = null
) {
    GONE,
    TRACK_NOT_FOUND(
        image = R.drawable.track_not_found,
        message = R.string.placeholder_message_not_found
    ),
    NETWORK_PROBLEM(
        image = R.drawable.network_problem,
        message = R.string.placeholder_message_network_problem,
        additionalMessage = R.string.placeholder_aditional_message_network_problems,
        button = R.string.update
    )
}