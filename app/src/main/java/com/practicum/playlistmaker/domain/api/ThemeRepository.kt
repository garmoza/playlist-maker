package com.practicum.playlistmaker.domain.api

interface ThemeRepository {
    fun darkThemeEnabled(defValue: Boolean): Boolean

    fun setDarkThemeEnabled(enabled: Boolean)
}