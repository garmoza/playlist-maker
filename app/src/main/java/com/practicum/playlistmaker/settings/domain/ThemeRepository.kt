package com.practicum.playlistmaker.settings.domain

interface ThemeRepository {
    fun darkThemeEnabled(defValue: Boolean): Boolean

    fun setDarkThemeEnabled(enabled: Boolean)
}