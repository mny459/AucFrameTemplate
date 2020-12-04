package com.mny.wan.pkg.extension

import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


fun Fragment.enterFullScreen() {
    activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
    activity?.window?.statusBarColor = Color.TRANSPARENT
    val flags = View.SYSTEM_UI_FLAG_LOW_PROFILE or
            View.SYSTEM_UI_FLAG_FULLSCREEN or
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
    activity?.window?.decorView?.systemUiVisibility = flags
    (activity as? AppCompatActivity)?.supportActionBar?.hide()
    // Delayed removal of status and navigation bar

    // Note that some of these constants are new as of API 16 (Jelly Bean)
    // and API 19 (KitKat). It is safe to use them, as they are inlined
    // at compile-time and do nothing on earlier devices.
    lifecycleScope.launch {
        delay(10)

    }
}

fun Activity.enterFullScreen() {
    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
        val decorView = window.decorView
        //View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN--能够使得我们的页面布局延伸到状态栏之下，但不会隐藏状态栏。也就相当于状态栏是遮盖在布局之上的
        //View.SYSTEM_UI_FLAG_FULLSCREEN -- 能够使得我们的页面布局延伸到状态栏，但是会隐藏状态栏。
        //WindowManager.LayoutParams.FLAG_FULLSCREEN
        decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)

        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = Color.TRANSPARENT
    }
}


fun Activity.lightStatusBar(light: Boolean) {
    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
        val decorView = window.decorView
        var visibility = decorView.systemUiVisibility
        visibility = if (light) {
            visibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        } else {
            visibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
        }
        decorView.systemUiVisibility = visibility
    }
}

fun Fragment.quitFullScreen() {
    activity?.quitFullScreen()
}

fun Activity.quitFullScreen() {
    window?.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
    window?.decorView?.systemUiVisibility = 0
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