package com.practicum.playlistmaker.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
import android.net.ConnectivityManager
import com.google.gson.Gson
import com.practicum.playlistmaker.common.data.network.NetworkClient
import com.practicum.playlistmaker.common.data.preferences.PLAYLIST_MAKER_PREFERENCES
import com.practicum.playlistmaker.search.data.network.ITunseApiService
import com.practicum.playlistmaker.search.data.network.ITunseRetrofitNetworkClient
import com.practicum.playlistmaker.sharing.data.ExternalNavigatorImpl
import com.practicum.playlistmaker.sharing.domain.ExternalNavigator
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val ITUNSE_BASE_URL = "https://itunes.apple.com"

val dataModule = module {
    single<SharedPreferences> {
        androidContext()
            .getSharedPreferences(PLAYLIST_MAKER_PREFERENCES, Application.MODE_PRIVATE)
    }

    single<Resources> {
        androidContext().resources
    }

    single<ITunseApiService> {
        Retrofit.Builder()
            .baseUrl(ITUNSE_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ITunseApiService::class.java)
    }

    single<NetworkClient> {
        val connectivityManager = androidContext().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        ITunseRetrofitNetworkClient(connectivityManager, get())
    }

    factory { Gson() }

    single<ExternalNavigator> {
        ExternalNavigatorImpl(androidContext())
    }
}