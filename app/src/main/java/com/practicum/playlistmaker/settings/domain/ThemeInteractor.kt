package com.practicum.playlistmaker.settings.domain

interface ThemeInteractor {
    fun darkThemeEnabled(): Boolean

    fun setDarkTheme()

    fun setLightTheme()
}