package com.practicum.playlistmaker.settings.domain

interface ThemeInteractor {
    fun darkThemeEnabled(defValue: Boolean = false): Boolean

    fun setDarkTheme()

    fun setLightTheme()
}