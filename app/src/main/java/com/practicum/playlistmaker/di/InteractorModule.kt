package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.search.domain.TracksInteractor
import com.practicum.playlistmaker.search.domain.TracksSearchHistoryInteractor
import com.practicum.playlistmaker.settings.domain.ThemeInteractor
import com.practicum.playlistmaker.sharing.domain.SharingInteractor
import org.koin.dsl.module

val interactorModule = module {
    single<TracksInteractor> {
        Creator.provideTracksInteractor()
    }

    single<TracksSearchHistoryInteractor> {
        Creator.provideTracksSearchHistoryInteractor()
    }

    single<ThemeInteractor> {
        Creator.provideThemeInteractor()
    }

    single<SharingInteractor> {
        Creator.provideSharingInteractor()
    }
}