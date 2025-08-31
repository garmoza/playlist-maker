package com.practicum.playlistmaker.common.ui

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val DEFAULT_CLICK_DELAY = 1000L

fun <T> debounceClick(
    delayMillis: Long = DEFAULT_CLICK_DELAY,
    coroutineScope: CoroutineScope,
    action: (T) -> Unit
): (T) -> Unit {
    var debounceJob: Job? = null
    return { param: T ->
        if (debounceJob == null || debounceJob?.isCompleted == true) {
            debounceJob = coroutineScope.launch {
                action(param)
                delay(delayMillis)
            }
        }
    }
}