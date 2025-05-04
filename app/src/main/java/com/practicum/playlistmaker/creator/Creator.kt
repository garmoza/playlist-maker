package com.practicum.playlistmaker.creator

import android.app.Application
import com.practicum.playlistmaker.sharing.data.ExternalNavigatorImpl
import com.practicum.playlistmaker.sharing.domain.ExternalNavigator

object Creator {

    lateinit var application: Application

    fun initApplication(application: Application) {
        Creator.application = application
    }

    fun getExternalNavigator(): ExternalNavigator =
        ExternalNavigatorImpl(application.applicationContext)
}