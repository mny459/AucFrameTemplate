package com.mny.wan.entension

import android.os.SystemClock
import android.view.View

/**
 * Desc:
 */
fun View.setOnDebouncedClickListener(action: () -> Unit) {
    val actionDebouncer = ActionDebouncer(action, 1000)

    // This is the only place in the project where we should actually use setOnClickListener
    setOnClickListener {
        actionDebouncer.notifyAction()
    }
}

fun View.removeOnDebouncedClickListener() {
    setOnClickListener(null)
//    isClickable = false
}

private class ActionDebouncer(private val action: () -> Unit, private val intervalTime: Long?) {

    companion object {
        const val DEBOUNCE_INTERVAL_MILLISECONDS = 600L
    }

    private var lastActionTime = 0L

    fun notifyAction() {
        val now = SystemClock.elapsedRealtime()

        val millisecondsPassed = now - lastActionTime
        val actionAllowed = millisecondsPassed > (intervalTime ?: DEBOUNCE_INTERVAL_MILLISECONDS)
        lastActionTime = now

        if (actionAllowed) {
            action.invoke()
        }
    }
}