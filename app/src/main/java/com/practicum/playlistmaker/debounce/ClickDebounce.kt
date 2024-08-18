package com.practicum.playlistmaker.debounce

import android.os.Handler
import android.os.Looper
import android.view.View

class ClickDebounce(looper: Looper) {

    private val handler = Handler(looper)
    private val clicked = mutableSetOf<View>()

    fun execute(task: Runnable, view: View) {
        if (!clicked.contains(view)) {
            clicked.add(view)
            handler.postDelayed({ clicked.remove(view) }, DEFAULT_DELAY)
            task.run()
        }
    }

    companion object {
        private const val DEFAULT_DELAY = 1000L
    }
}