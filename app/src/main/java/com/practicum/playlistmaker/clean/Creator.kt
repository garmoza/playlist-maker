package com.practicum.playlistmaker.clean

import android.content.SharedPreferences
import android.widget.ImageButton
import android.widget.TextView
import com.practicum.playlistmaker.clean.data.TracksRepositoryImpl
import com.practicum.playlistmaker.clean.data.TracksSearchHistoryRepositoryImpl
import com.practicum.playlistmaker.clean.data.network.ITunseRetrofitNetworkClient
import com.practicum.playlistmaker.clean.domain.api.TracksInteractor
import com.practicum.playlistmaker.clean.domain.api.TracksRepository
import com.practicum.playlistmaker.clean.domain.api.TracksSearchHistoryInteractor
import com.practicum.playlistmaker.clean.domain.api.TracksSearchHistoryRepository
import com.practicum.playlistmaker.clean.domain.impl.TracksInteractorImpl
import com.practicum.playlistmaker.clean.domain.impl.TracksSearchHistoryInteractorImpl
import com.practicum.playlistmaker.clean.presentation.player.Player
import com.practicum.playlistmaker.clean.presentation.player.PlayerImpl

object Creator {

    fun provideTracksInteractor(): TracksInteractor {
        return TracksInteractorImpl(getTracksRepository())
    }

    private fun getTracksRepository(): TracksRepository {
        return TracksRepositoryImpl(ITunseRetrofitNetworkClient())
    }

    fun provideTracksSearchHistoryInteractor(sharedPreferences: SharedPreferences): TracksSearchHistoryInteractor =
        TracksSearchHistoryInteractorImpl(getSearchHistoryRepository(sharedPreferences))

    private fun getSearchHistoryRepository(sharedPreferences: SharedPreferences): TracksSearchHistoryRepository =
        TracksSearchHistoryRepositoryImpl(sharedPreferences)

    fun providePlayer(playButton: ImageButton, playtimeTextView: TextView): Player =
        PlayerImpl(playButton, playtimeTextView)
}