package com.practicum.playlistmaker.settings.domain

interface ThemeRepository {
    fun darkThemeEnabled(): Boolean

    fun setDarkThemeEnabled(enabled: Boolean)
}