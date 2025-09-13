package com.practicum.playlistmaker.di

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
    single<TracksRepository> {
        TracksRepositoryImpl(get(), get())
    }

    single<TracksSearchHistoryRepository> {
        TracksSearchHistoryRepositoryImpl(get(), get(), get())
    }

    single<ThemeRepository> {
        ThemeRepositoryImpl(get(), get())
    }

    single<FavouriteTracksRepository> {
        FavouriteTracksRepositoryImpl(get())
    }
}