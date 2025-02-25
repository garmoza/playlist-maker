package com.practicum.playlistmaker.search.data.network

import android.net.ConnectivityManager
import com.practicum.playlistmaker.search.data.dto.ITunseSearchRequest
import com.practicum.playlistmaker.common.data.dto.Response
import com.practicum.playlistmaker.common.data.network.AbstractNetworkClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ITunseRetrofitNetworkClient(
    connectivityManager: ConnectivityManager
) : AbstractNetworkClient(connectivityManager) {

    private val retrofit = Retrofit.Builder()
        .baseUrl(ITUNSE_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val iTunseService = retrofit.create(ITunseApiService::class.java)

    override fun doRequest(dto: Any): Response {
        if (!isConnected()) {
            return Response().apply { resultCode = -1 }
        }

        return try {
            if (dto is ITunseSearchRequest) {
                val resp = iTunseService.search(dto.expression).execute()

                val body = resp.body() ?: Response()

                body.apply { resultCode = resp.code() }
            } else {
                Response().apply { resultCode = 400 }
            }
        } catch (e: Exception) {
            Response().apply { resultCode = 400 }
        }
    }


    companion object {
        private const val ITUNSE_BASE_URL = "https://itunes.apple.com"
    }
}