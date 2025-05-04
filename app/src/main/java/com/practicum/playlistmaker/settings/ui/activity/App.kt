package com.practicum.playlistmaker.settings.ui.activity

import android.app.Application
import com.practicum.playlistmaker.di.dataModule
import com.practicum.playlistmaker.di.interactorModule
import com.practicum.playlistmaker.di.repositoryModule
import com.practicum.playlistmaker.di.viewModelModule
import com.practicum.playlistmaker.settings.domain.ThemeInteractor
import org.koin.android.ext.android.getKoin
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

    private lateinit var themeInteractor: ThemeInteractor

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(dataModule, repositoryModule, interactorModule, viewModelModule)
        }

        themeInteractor = getKoin().get<ThemeInteractor>()

        if (themeInteractor.darkThemeEnabled()) {
            themeInteractor.setDarkTheme()
        } else {
            themeInteractor.setLightTheme()
        }
    }
}