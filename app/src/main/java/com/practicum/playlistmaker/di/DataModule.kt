package com.practicum.playlistmaker.di

import android.app.Application
import android.content.SharedPreferences
import com.practicum.playlistmaker.common.data.preferences.PLAYLIST_MAKER_PREFERENCES
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataModule = module {
    single<SharedPreferences> {
        androidContext()
            .getSharedPreferences(PLAYLIST_MAKER_PREFERENCES, Application.MODE_PRIVATE)
    }
}