package com.practicum.playlistmaker.settings.ui.activity

import android.app.Application
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.settings.domain.api.ThemeInteractor

class App : Application() {

    private lateinit var themeInteractor: ThemeInteractor

    override fun onCreate() {
        super.onCreate()

        Creator.initApplication(this)

        themeInteractor = Creator.provideThemeInteractor()

        val darkThemeEnabled = themeInteractor.darkThemeEnabled(isUiModeNight())

        renderTheme(darkThemeEnabled)
    }

    private fun isUiModeNight() =
        resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES

    private fun renderTheme(darkThemeEnabled: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        when (darkThemeEnabled) {
            true -> themeInteractor.setDarkTheme()
            false ->themeInteractor.setLightTheme()
        }

        renderTheme(darkThemeEnabled)
    }
}