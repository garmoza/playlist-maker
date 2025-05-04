package com.practicum.playlistmaker.search.data.network

import android.net.ConnectivityManager
import com.practicum.playlistmaker.search.data.dto.ITunseSearchRequest
import com.practicum.playlistmaker.common.data.dto.Response
import com.practicum.playlistmaker.common.data.network.AbstractNetworkClient

class ITunseRetrofitNetworkClient(
    connectivityManager: ConnectivityManager,
    private val iTunseService: ITunseApiService
) : AbstractNetworkClient(connectivityManager) {

    override fun doRequest(dto: Any): Response {
        if (!isConnected()) {
            return Response().apply { resultCode = -1 }
        }

        return try {
            if (dto is ITunseSearchRequest) {
                val response = iTunseService.search(dto.expression).execute()

                val body = response.body() ?: Response()

                body.apply { resultCode = response.code() }
            } else {
                Response().apply { resultCode = 400 }
            }
        } catch (e: Exception) {
            Response().apply { resultCode = 400 }
        }
    }
}