package com.mny.wan.pkg.extension

import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun Fragment.enterFullScreen() {
    activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
    // Delayed removal of status and navigation bar

    // Note that some of these constants are new as of API 16 (Jelly Bean)
    // and API 19 (KitKat). It is safe to use them, as they are inlined
    // at compile-time and do nothing on earlier devices.
    lifecycleScope.launch {
        delay(10)
        val flags = View.SYSTEM_UI_FLAG_LOW_PROFILE or
                View.SYSTEM_UI_FLAG_FULLSCREEN or
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        activity?.window?.decorView?.systemUiVisibility = flags
        (activity as? AppCompatActivity)?.supportActionBar?.hide()
    }

}

fun Fragment.quitFullScreen() {
    activity?.apply {
        window?.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        window?.decorView?.systemUiVisibility = 0
    }
}

@Suppress("InlinedApi")
private fun show(content: View) {
    // Show the system bar
    content.systemUiVisibility =
        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//    visible = true
//
//    // Schedule a runnable to display UI elements after a delay
//    hideHandler.removeCallbacks(hidePart2Runnable)
//    hideHandler.postDelayed(showPart2Runnable, UI_ANIMATION_DELAY.toLong())
//    (activity as? AppCompatActivity)?.supportActionBar?.show()
}