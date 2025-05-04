package com.practicum.playlistmaker.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
import android.net.ConnectivityManager
import com.practicum.playlistmaker.common.data.network.NetworkClient
import com.practicum.playlistmaker.common.data.preferences.PLAYLIST_MAKER_PREFERENCES
import com.practicum.playlistmaker.palyer.data.PlayerImpl
import com.practicum.playlistmaker.palyer.domain.Player
import com.practicum.playlistmaker.search.data.network.ITunseRetrofitNetworkClient
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataModule = module {
    single<SharedPreferences> {
        androidContext()
            .getSharedPreferences(PLAYLIST_MAKER_PREFERENCES, Application.MODE_PRIVATE)
    }

    single<Resources> {
        androidContext().resources
    }

    single<NetworkClient> {
        val connectivityManager = androidContext().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        ITunseRetrofitNetworkClient(connectivityManager)
    }

    factory<Player> {
        PlayerImpl()
    }
}