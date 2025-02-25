package com.practicum.playlistmaker.settings.domain.api

interface ThemeRepository {
    fun darkThemeEnabled(defValue: Boolean): Boolean

    fun setDarkThemeEnabled(enabled: Boolean)
}