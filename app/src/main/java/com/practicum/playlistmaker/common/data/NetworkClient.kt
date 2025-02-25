package com.practicum.playlistmaker.common.data

import com.practicum.playlistmaker.common.data.dto.Response

interface NetworkClient {
    fun doRequest(dto: Any): Response
}