package com.practicum.playlistmaker.sharing.domain.model

data class EmailData(
    val sendToAddresses: List<String>,
    val subject: String,
    val body: String
)
