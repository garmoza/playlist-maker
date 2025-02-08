package com.practicum.playlistmaker

import android.app.Application
import android.content.SharedPreferences
import android.widget.ImageButton
import android.widget.TextView
import com.practicum.playlistmaker.data.ThemeRepositoryImpl
import com.practicum.playlistmaker.data.TracksRepositoryImpl
import com.practicum.playlistmaker.data.TracksSearchHistoryRepositoryImpl
import com.practicum.playlistmaker.data.network.ITunseRetrofitNetworkClient
import com.practicum.playlistmaker.domain.api.ThemeInteractor
import com.practicum.playlistmaker.domain.api.ThemeRepository
import com.practicum.playlistmaker.domain.api.TracksInteractor
import com.practicum.playlistmaker.domain.api.TracksRepository
import com.practicum.playlistmaker.domain.api.TracksSearchHistoryInteractor
import com.practicum.playlistmaker.domain.api.TracksSearchHistoryRepository
import com.practicum.playlistmaker.domain.impl.ThemeInteractorImpl
import com.practicum.playlistmaker.domain.impl.TracksInteractorImpl
import com.practicum.playlistmaker.domain.impl.TracksSearchHistoryInteractorImpl
import com.practicum.playlistmaker.presentation.player.Player
import com.practicum.playlistmaker.presentation.player.PlayerImpl
import com.practicum.playlistmaker.data.preferences.PLAYLIST_MAKER_PREFERENCES

object Creator {

    private lateinit var application: Application

    fun initApplication(application: Application) {
        Creator.application = application
    }

    private fun getSharedPreferences(): SharedPreferences =
        application.getSharedPreferences(PLAYLIST_MAKER_PREFERENCES, Application.MODE_PRIVATE)

    fun provideTracksInteractor(): TracksInteractor =
        TracksInteractorImpl(getTracksRepository())

    private fun getTracksRepository(): TracksRepository =
        TracksRepositoryImpl(ITunseRetrofitNetworkClient())

    fun provideTracksSearchHistoryInteractor(): TracksSearchHistoryInteractor =
        TracksSearchHistoryInteractorImpl(getSearchHistoryRepository())

    private fun getSearchHistoryRepository(): TracksSearchHistoryRepository =
        TracksSearchHistoryRepositoryImpl(getSharedPreferences())

    fun provideThemeInteractor(): ThemeInteractor =
        ThemeInteractorImpl(getThemeRepository())

    private fun getThemeRepository(): ThemeRepository =
        ThemeRepositoryImpl(getSharedPreferences())

    fun providePlayer(playButton: ImageButton, playtimeTextView: TextView): Player =
        PlayerImpl(playButton, playtimeTextView)
}