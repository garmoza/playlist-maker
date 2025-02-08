package com.practicum.playlistmaker.clean.domain.api

interface ThemeInteractor {
    fun darkThemeEnabled(defValue: Boolean = false): Boolean

    fun setDarkTheme()

    fun setLightTheme()
}