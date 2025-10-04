package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.common.domain.models.Track
import com.practicum.playlistmaker.favourite.ui.view_model.FavoritesTracksViewModel
import com.practicum.playlistmaker.library.ui.view_model.AddPlaylistViewModel
import com.practicum.playlistmaker.library.ui.view_model.PlaylistsViewModel
import com.practicum.playlistmaker.palyer.ui.view_model.MediaPlayerViewModel
import com.practicum.playlistmaker.search.ui.view_model.SearchViewModel
import com.practicum.playlistmaker.settings.ui.view_model.SettingsViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        SettingsViewModel(get(), get())
    }

    viewModel {
        SearchViewModel(get(), get())
    }

    viewModel { (track: Track) ->
        MediaPlayerViewModel(get(), track)
    }

    viewModel { FavoritesTracksViewModel(get()) }

    viewModel { PlaylistsViewModel() }

    viewModel { AddPlaylistViewModel(get(), androidApplication()) }
}