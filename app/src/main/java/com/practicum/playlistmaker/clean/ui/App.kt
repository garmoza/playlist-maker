package com.practicum.playlistmaker.clean.ui

import android.app.Application
import android.content.SharedPreferences
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import com.practicum.playlistmaker.clean.Creator
import com.practicum.playlistmaker.clean.presentation.preferences.DARK_THEME_KEY

class App : Application() {

    var darkTheme: Boolean = false
        private set

    private lateinit var sharedPrefs: SharedPreferences

    override fun onCreate() {
        super.onCreate()

        Creator.initApplication(this)

        sharedPrefs = Creator.getSharedPreferences()

        darkTheme = sharedPrefs.getBoolean(DARK_THEME_KEY, isUiModeNight())
        switchTheme(darkTheme)
    }

    private fun isUiModeNight() =
        resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
        sharedPrefs.edit()
            .putBoolean(DARK_THEME_KEY, darkTheme)
            .apply()
    }
}