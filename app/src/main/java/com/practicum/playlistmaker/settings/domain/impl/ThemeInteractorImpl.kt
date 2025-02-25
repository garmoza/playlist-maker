package com.practicum.playlistmaker.settings.domain.impl

import com.practicum.playlistmaker.settings.domain.api.ThemeInteractor
import com.practicum.playlistmaker.settings.domain.api.ThemeRepository

class ThemeInteractorImpl(
    private val themeRepository: ThemeRepository
) : ThemeInteractor {
    override fun darkThemeEnabled(defValue: Boolean): Boolean =
        themeRepository.darkThemeEnabled(defValue)

    override fun setDarkTheme() {
        themeRepository.setDarkThemeEnabled(true)
    }

    override fun setLightTheme() {
        themeRepository.setDarkThemeEnabled(false)
    }
}