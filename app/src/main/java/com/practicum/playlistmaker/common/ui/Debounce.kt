package com.practicum.playlistmaker.common.ui

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

const val DEBOUNCE_CLICK_DELAY_DEFAULT = 1000L
const val DEBOUNCE_REQUEST_DELAY_DEFAULT = 2000L
const val DEBOUNCE_DELAY_NONE = 0L

fun <T> debounceRequest(
    coroutineScope: CoroutineScope,
    action: (T) -> Unit
): (T, delayMillis: Long) -> Job? {
    var debounceJob: Job? = null
    return { param: T, delayMillis: Long ->
        debounceJob?.cancel()
        debounceJob = coroutineScope.launch {
            delay(delayMillis)
            action(param)
        }
        debounceJob
    }
}

fun <T> debounceClick(
    delayMillis: Long = DEBOUNCE_CLICK_DELAY_DEFAULT,
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