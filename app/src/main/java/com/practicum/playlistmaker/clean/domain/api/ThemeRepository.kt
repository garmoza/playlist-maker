package com.practicum.playlistmaker.clean.domain.api

interface ThemeRepository {
    fun darkThemeEnabled(defValue: Boolean): Boolean

    fun setDarkThemeEnabled(enabled: Boolean)
}