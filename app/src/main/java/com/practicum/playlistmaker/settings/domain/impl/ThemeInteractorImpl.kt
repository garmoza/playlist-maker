package com.practicum.playlistmaker.settings.domain.impl

import com.practicum.playlistmaker.settings.domain.ThemeInteractor
import com.practicum.playlistmaker.settings.domain.ThemeRepository

class ThemeInteractorImpl(
    private val themeRepository: ThemeRepository
) : ThemeInteractor {
    override fun darkThemeEnabled(): Boolean =
        themeRepository.darkThemeEnabled()

    override fun setDarkTheme() {
        themeRepository.setDarkThemeEnabled(true)
    }

    override fun setLightTheme() {
        themeRepository.setDarkThemeEnabled(false)
    }
}