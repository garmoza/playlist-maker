package com.practicum.playlistmaker.client

import com.practicum.playlistmaker.client.ITunseTracksResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunseApi {

    @GET("/search?entity=song")
    fun search(@Query("term") text: String): Call<ITunseTracksResponse>
}