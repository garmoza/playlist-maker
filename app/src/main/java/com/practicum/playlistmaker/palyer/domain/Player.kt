package com.practicum.playlistmaker.palyer.domain

interface Player {

    fun prepare(url: String)

    fun play()

    fun pause()

    fun release()

    fun switchBetweenPlayAndPause()
}