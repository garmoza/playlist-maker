package com.practicum.playlistmaker.settings.data

import android.content.SharedPreferences
import android.content.res.Configuration
import android.content.res.Resources
import androidx.appcompat.app.AppCompatDelegate
import com.practicum.playlistmaker.common.data.preferences.DARK_THEME_KEY
import com.practicum.playlistmaker.settings.domain.ThemeRepository

class ThemeRepositoryImpl(
    private val sharedPreferences: SharedPreferences,
    private val resources: Resources
) : ThemeRepository {
    override fun darkThemeEnabled(): Boolean =
        sharedPreferences.getBoolean(DARK_THEME_KEY, isUiModeNight())

    private fun isUiModeNight() =
        resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES

    override fun setDarkThemeEnabled(enabled: Boolean) {
        sharedPreferences.edit()
            .putBoolean(DARK_THEME_KEY, enabled)
            .apply()

        AppCompatDelegate.setDefaultNightMode(
            if (enabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}