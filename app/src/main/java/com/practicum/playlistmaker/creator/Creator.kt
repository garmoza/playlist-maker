package com.practicum.playlistmaker.creator

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import com.practicum.playlistmaker.settings.data.ThemeRepositoryImpl
import com.practicum.playlistmaker.search.data.TracksRepositoryImpl
import com.practicum.playlistmaker.search.data.network.ITunseRetrofitNetworkClient
import com.practicum.playlistmaker.settings.domain.ThemeRepository
import com.practicum.playlistmaker.search.domain.TracksRepository
import com.practicum.playlistmaker.palyer.domain.Player
import com.practicum.playlistmaker.palyer.data.PlayerImpl
import com.practicum.playlistmaker.common.data.preferences.PLAYLIST_MAKER_PREFERENCES
import com.practicum.playlistmaker.sharing.data.ExternalNavigatorImpl
import com.practicum.playlistmaker.sharing.domain.ExternalNavigator

object Creator {

    lateinit var application: Application

    fun initApplication(application: Application) {
        Creator.application = application
    }

    fun getSharedPreferences(): SharedPreferences =
        application.getSharedPreferences(PLAYLIST_MAKER_PREFERENCES, Application.MODE_PRIVATE)

    fun getTracksRepository(): TracksRepository {
        val connectivityManager = application.getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        return TracksRepositoryImpl(ITunseRetrofitNetworkClient(connectivityManager))
    }

    fun getThemeRepository(): ThemeRepository =
        ThemeRepositoryImpl(getSharedPreferences(), application.resources)

    fun providePlayer(): Player =
        PlayerImpl()

    fun getExternalNavigator(): ExternalNavigator =
        ExternalNavigatorImpl(application.applicationContext)
}