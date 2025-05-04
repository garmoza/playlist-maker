package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.search.domain.TracksRepository
import com.practicum.playlistmaker.search.domain.TracksSearchHistoryRepository
import com.practicum.playlistmaker.settings.domain.ThemeRepository
import org.koin.dsl.module

val repositoryModule = module {
    single<TracksRepository> {
        Creator.getTracksRepository()
    }

    single<TracksSearchHistoryRepository> {
        Creator.getSearchHistoryRepository()
    }

    single<ThemeRepository> {
        Creator.getThemeRepository()
    }
}