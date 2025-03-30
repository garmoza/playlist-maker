package com.practicum.playlistmaker.settings.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.settings.domain.ThemeInteractor
import com.practicum.playlistmaker.settings.domain.model.ThemeMode
import com.practicum.playlistmaker.settings.domain.model.ThemeSettings
import com.practicum.playlistmaker.sharing.domain.SharingInteractor
import com.practicum.playlistmaker.sharing.domain.model.EmailData

class SettingsViewModel(
    private val sharingInteractor: SharingInteractor,
    private val themeInteractor: ThemeInteractor
) : ViewModel() {

    companion object {
        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                SettingsViewModel(
                    Creator.provideSharingInteractor(),
                    Creator.provideThemeInteractor()
                )
            }
        }
    }

    private val themeSettingsLiveData = MutableLiveData<ThemeSettings>()

    init {
        val themeMode = if (themeInteractor.darkThemeEnabled()) {
            ThemeMode.DARK
        } else {
            ThemeMode.LIGHT
        }
        themeSettingsLiveData.value = getCurrentThemeSettings().copy(themeMode = themeMode)
    }

    fun getThemeSettingsLiveData(): LiveData<ThemeSettings> = themeSettingsLiveData

    fun setThemeMode(themeMode: ThemeMode) {
        when (themeMode) {
            ThemeMode.LIGHT -> themeInteractor.setLightTheme()
            ThemeMode.DARK -> themeInteractor.setDarkTheme()
        }

        themeSettingsLiveData.value = getCurrentThemeSettings().copy(themeMode = themeMode)
    }

    private fun getCurrentThemeSettings(): ThemeSettings {
        return themeSettingsLiveData.value ?: ThemeSettings(themeMode = ThemeMode.LIGHT)
    }

    fun shareApp(link: String) {
        sharingInteractor.shareApp(link)
    }

    fun openTerms(link: String) {
        sharingInteractor.openTerms(link)
    }

    fun openSupport(emailData: EmailData) {
        sharingInteractor.openSupport(emailData)
    }
}