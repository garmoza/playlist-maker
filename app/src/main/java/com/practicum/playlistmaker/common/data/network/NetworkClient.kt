package com.practicum.playlistmaker.common.data.network

import com.practicum.playlistmaker.common.data.dto.Response

interface NetworkClient {
    suspend fun doRequest(dto: Any): Response
}