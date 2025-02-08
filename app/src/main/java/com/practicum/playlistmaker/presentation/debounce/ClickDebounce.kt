package com.practicum.playlistmaker.presentation.debounce

import android.os.Handler
import android.os.Looper

class ClickDebounce(looper: Looper) {

    private val handler = Handler(looper)
    private val clicked = mutableSetOf<Int>()

    fun execute(task: Runnable, viewId: Int) {
        if (!clicked.contains(viewId)) {
            clicked.add(viewId)
            handler.postDelayed({ clicked.remove(viewId) }, DEFAULT_DELAY)
            task.run()
        }
    }

    companion object {
        private const val DEFAULT_DELAY = 1000L
    }
}