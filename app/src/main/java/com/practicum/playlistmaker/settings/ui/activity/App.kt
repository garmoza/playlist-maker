package com.practicum.playlistmaker.settings.ui.activity

import android.app.Application
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.settings.domain.ThemeInteractor

class App : Application() {

    private lateinit var themeInteractor: ThemeInteractor

    override fun onCreate() {
        super.onCreate()

        Creator.initApplication(this)

        themeInteractor = Creator.provideThemeInteractor()

        if (themeInteractor.darkThemeEnabled()) {
            themeInteractor.setDarkTheme()
        } else {
            themeInteractor.setLightTheme()
        }
    }
}