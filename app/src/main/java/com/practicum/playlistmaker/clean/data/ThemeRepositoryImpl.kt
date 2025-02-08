package com.practicum.playlistmaker.clean.data

import android.content.SharedPreferences
import com.practicum.playlistmaker.clean.domain.api.ThemeRepository

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