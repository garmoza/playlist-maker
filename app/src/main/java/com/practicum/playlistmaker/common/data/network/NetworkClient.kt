package com.practicum.playlistmaker.common.data.network

import com.practicum.playlistmaker.common.data.dto.Response

interface NetworkClient {
    fun doRequest(dto: Any): Response
}