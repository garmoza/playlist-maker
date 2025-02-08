package com.practicum.playlistmaker.clean

import android.app.Application
import android.content.SharedPreferences
import android.widget.ImageButton
import android.widget.TextView
import com.practicum.playlistmaker.clean.data.ThemeRepositoryImpl
import com.practicum.playlistmaker.clean.data.TracksRepositoryImpl
import com.practicum.playlistmaker.clean.data.TracksSearchHistoryRepositoryImpl
import com.practicum.playlistmaker.clean.data.network.ITunseRetrofitNetworkClient
import com.practicum.playlistmaker.clean.domain.api.ThemeInteractor
import com.practicum.playlistmaker.clean.domain.api.ThemeRepository
import com.practicum.playlistmaker.clean.domain.api.TracksInteractor
import com.practicum.playlistmaker.clean.domain.api.TracksRepository
import com.practicum.playlistmaker.clean.domain.api.TracksSearchHistoryInteractor
import com.practicum.playlistmaker.clean.domain.api.TracksSearchHistoryRepository
import com.practicum.playlistmaker.clean.domain.impl.ThemeInteractorImpl
import com.practicum.playlistmaker.clean.domain.impl.TracksInteractorImpl
import com.practicum.playlistmaker.clean.domain.impl.TracksSearchHistoryInteractorImpl
import com.practicum.playlistmaker.clean.presentation.player.Player
import com.practicum.playlistmaker.clean.presentation.player.PlayerImpl
import com.practicum.playlistmaker.clean.data.PLAYLIST_MAKER_PREFERENCES

object Creator {

    private lateinit var application: Application

    fun initApplication(application: Application) {
        this.application = application
    }

    fun getSharedPreferences(): SharedPreferences =
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