package com.practicum.playlistmaker.clean.data.network

import com.practicum.playlistmaker.clean.data.dto.ITunseTracksResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunseApiService {

    @GET("/search?entity=song")
    fun search(@Query("term") text: String): Call<ITunseTracksResponse>
}