package com.practicum.playlistmaker.creator

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import com.practicum.playlistmaker.settings.data.ThemeRepositoryImpl
import com.practicum.playlistmaker.search.data.TracksRepositoryImpl
import com.practicum.playlistmaker.search.data.TracksSearchHistoryRepositoryImpl
import com.practicum.playlistmaker.search.data.network.ITunseRetrofitNetworkClient
import com.practicum.playlistmaker.settings.domain.ThemeInteractor
import com.practicum.playlistmaker.settings.domain.ThemeRepository
import com.practicum.playlistmaker.search.domain.TracksInteractor
import com.practicum.playlistmaker.search.domain.TracksRepository
import com.practicum.playlistmaker.search.domain.TracksSearchHistoryInteractor
import com.practicum.playlistmaker.search.domain.TracksSearchHistoryRepository
import com.practicum.playlistmaker.settings.domain.impl.ThemeInteractorImpl
import com.practicum.playlistmaker.search.domain.impl.TracksInteractorImpl
import com.practicum.playlistmaker.search.domain.impl.TracksSearchHistoryInteractorImpl
import com.practicum.playlistmaker.palyer.domain.Player
import com.practicum.playlistmaker.palyer.data.PlayerImpl
import com.practicum.playlistmaker.common.data.preferences.PLAYLIST_MAKER_PREFERENCES
import com.practicum.playlistmaker.sharing.data.ExternalNavigatorImpl
import com.practicum.playlistmaker.sharing.domain.ExternalNavigator
import com.practicum.playlistmaker.sharing.domain.SharingInteractor
import com.practicum.playlistmaker.sharing.domain.impl.SharingInteractorImpl

object Creator {

    private lateinit var application: Application

    fun initApplication(application: Application) {
        Creator.application = application
    }

    private fun getSharedPreferences(): SharedPreferences =
        application.getSharedPreferences(PLAYLIST_MAKER_PREFERENCES, Application.MODE_PRIVATE)

    fun provideTracksInteractor(): TracksInteractor =
        TracksInteractorImpl(getTracksRepository())

    private fun getTracksRepository(): TracksRepository {
        val connectivityManager = application.getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        return TracksRepositoryImpl(ITunseRetrofitNetworkClient(connectivityManager))
    }


    fun provideTracksSearchHistoryInteractor(): TracksSearchHistoryInteractor =
        TracksSearchHistoryInteractorImpl(getSearchHistoryRepository())

    private fun getSearchHistoryRepository(): TracksSearchHistoryRepository =
        TracksSearchHistoryRepositoryImpl(getSharedPreferences())

    fun provideThemeInteractor(): ThemeInteractor =
        ThemeInteractorImpl(getThemeRepository())

    private fun getThemeRepository(): ThemeRepository =
        ThemeRepositoryImpl(getSharedPreferences(), application.resources)

    fun providePlayer(): Player =
        PlayerImpl()

    fun provideSharingInteractor(): SharingInteractor =
        SharingInteractorImpl(getExternalNavigator())

    private fun getExternalNavigator(): ExternalNavigator =
        ExternalNavigatorImpl(application.applicationContext)
}