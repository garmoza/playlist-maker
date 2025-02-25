package com.practicum.playlistmaker.search.data.network

import com.practicum.playlistmaker.common.data.NetworkClient
import com.practicum.playlistmaker.search.data.dto.ITunseSearchRequest
import com.practicum.playlistmaker.common.data.dto.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ITunseRetrofitNetworkClient : NetworkClient {

    private val retrofit = Retrofit.Builder()
        .baseUrl(ITUNSE_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val iTunseService = retrofit.create(ITunseApiService::class.java)

    override fun doRequest(dto: Any): Response =
        try {
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


    companion object {
        private const val ITUNSE_BASE_URL = "https://itunes.apple.com"
    }
}