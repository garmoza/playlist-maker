package com.practicum.playlistmaker.settings.data

import android.content.SharedPreferences
import com.practicum.playlistmaker.common.data.preferences.DARK_THEME_KEY
import com.practicum.playlistmaker.settings.domain.api.ThemeRepository

class ThemeRepositoryImpl(
    private val sharedPreferences: SharedPreferences
) : ThemeRepository {
    override fun darkThemeEnabled(defValue: Boolean): Boolean =
        sharedPreferences.getBoolean(DARK_THEME_KEY, defValue)

    override fun setDarkThemeEnabled(enabled: Boolean) {
        sharedPreferences.edit()
            .putBoolean(DARK_THEME_KEY, enabled)
            .apply()
    }
}