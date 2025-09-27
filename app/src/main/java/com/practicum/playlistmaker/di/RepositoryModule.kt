package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.common.data.db.AppDatabase
import com.practicum.playlistmaker.favourite.data.FavouriteTracksRepositoryImpl
import com.practicum.playlistmaker.favourite.domain.FavouriteTracksRepository
import com.practicum.playlistmaker.search.data.TracksRepositoryImpl
import com.practicum.playlistmaker.search.data.TracksSearchHistoryRepositoryImpl
import com.practicum.playlistmaker.search.domain.TracksRepository
import com.practicum.playlistmaker.search.domain.TracksSearchHistoryRepository
import com.practicum.playlistmaker.settings.data.ThemeRepositoryImpl
import com.practicum.playlistmaker.settings.domain.ThemeRepository
import org.koin.dsl.module

val repositoryModule = module {
    factory<TracksRepository> {
        TracksRepositoryImpl(get())
    }

    single<TracksSearchHistoryRepository> {
        TracksSearchHistoryRepositoryImpl(get(), get())
    }

    factory<ThemeRepository> {
        ThemeRepositoryImpl(get(), get())
    }

    factory<FavouriteTracksRepository> {
        FavouriteTracksRepositoryImpl(get<AppDatabase>().favouriteTrackDao())
    }
}