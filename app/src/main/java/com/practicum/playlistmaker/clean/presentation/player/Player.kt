package com.practicum.playlistmaker.clean.presentation.player

interface Player {

    fun prepare(url: String)

    fun play()

    fun pause()

    fun release()

    fun switchBetweenPlayAndPause()
}