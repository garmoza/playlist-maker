package com.practicum.playlistmaker.sharing.domain

import com.practicum.playlistmaker.sharing.domain.model.EmailData

interface SharingInteractor {
    fun shareApp(link: String)
    fun openTerms(link: String)
    fun openSupport(emailData: EmailData)
}