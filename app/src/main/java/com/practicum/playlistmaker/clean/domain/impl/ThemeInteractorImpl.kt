package com.practicum.playlistmaker.clean.domain.impl

import com.practicum.playlistmaker.clean.domain.api.ThemeInteractor
import com.practicum.playlistmaker.clean.domain.api.ThemeRepository

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