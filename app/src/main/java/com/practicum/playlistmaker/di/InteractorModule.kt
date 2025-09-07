package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.favourite.domain.FavouriteTracksInteractor
import com.practicum.playlistmaker.favourite.domain.impl.FavouriteTracksInteractorImpl
import com.practicum.playlistmaker.search.domain.TracksInteractor
import com.practicum.playlistmaker.search.domain.TracksSearchHistoryInteractor
import com.practicum.playlistmaker.search.domain.impl.TracksInteractorImpl
import com.practicum.playlistmaker.search.domain.impl.TracksSearchHistoryInteractorImpl
import com.practicum.playlistmaker.settings.domain.ThemeInteractor
import com.practicum.playlistmaker.settings.domain.impl.ThemeInteractorImpl
import com.practicum.playlistmaker.sharing.domain.SharingInteractor
import com.practicum.playlistmaker.sharing.domain.impl.SharingInteractorImpl
import org.koin.dsl.module

val interactorModule = module {
    single<TracksInteractor> {
        TracksInteractorImpl(get())
    }

    single<TracksSearchHistoryInteractor> {
        TracksSearchHistoryInteractorImpl(get())
    }

    single<ThemeInteractor> {
        ThemeInteractorImpl(get())
    }

    single<SharingInteractor> {
        SharingInteractorImpl(get())
    }

    single<FavouriteTracksInteractor> {
        FavouriteTracksInteractorImpl(get())
    }
}