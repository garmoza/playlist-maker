package com.practicum.playlistmaker.common.ui.debounce

import android.os.Handler
import android.os.Looper

class SearchDebounce(looper: Looper) {

    private val handler = Handler(looper)

    fun execute(task: Runnable, delay: Long = DEFAULT_DELAY) {
        handler.removeCallbacks(task)
        handler.postDelayed(task, delay)
    }

    fun remove(task: Runnable) {
        handler.removeCallbacks(task)
    }

    companion object {
        const val DEFAULT_DELAY = 2000L
        const val NONE_DELAY = 0L
    }
}