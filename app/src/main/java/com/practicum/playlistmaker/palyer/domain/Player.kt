package com.practicum.playlistmaker.palyer.domain

interface Player {

    fun prepare(url: String, statusObserver: StatusObserver)

    fun play()

    fun pause()

    fun release()

    fun switchBetweenPlayAndPause()

    interface StatusObserver {
        fun onPrepared()
        fun onPlay()
        fun onPause()
        fun onProgress(progress: Int)
    }
}