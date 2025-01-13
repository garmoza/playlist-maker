package com.practicum.playlistmaker.clean.data

import com.practicum.playlistmaker.clean.data.dto.Response

interface NetworkClient {
    fun doRequest(dto: Any): Response
}