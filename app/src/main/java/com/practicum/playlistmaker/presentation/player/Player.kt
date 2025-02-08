package com.practicum.playlistmaker.presentation.player

interface Player {

    fun prepare(url: String)

    fun play()

    fun pause()

    fun release()

    fun switchBetweenPlayAndPause()
}