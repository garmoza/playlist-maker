package com.practicum.playlistmaker.search.data.network

import android.net.ConnectivityManager
import com.practicum.playlistmaker.search.data.dto.ITunseSearchRequest
import com.practicum.playlistmaker.common.data.dto.Response
import com.practicum.playlistmaker.common.data.network.AbstractNetworkClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ITunseRetrofitNetworkClient(
    connectivityManager: ConnectivityManager,
    private val iTunseService: ITunseApiService
) : AbstractNetworkClient(connectivityManager) {

    override suspend fun doRequest(dto: Any): Response {
        if (!isConnected()) {
            return Response().apply { resultCode = -1 }
        }

        if (dto !is ITunseSearchRequest) {
            return Response().apply { resultCode = 400 }
        }

        return withContext(Dispatchers.IO) {
            try {
                val response = iTunseService.search(dto.expression)
                response.apply { resultCode = 200 }
            } catch (e: Throwable) {
                Response().apply { resultCode = 500 }
            }
        }
    }
}