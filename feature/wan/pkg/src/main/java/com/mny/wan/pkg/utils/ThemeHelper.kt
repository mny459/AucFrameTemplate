package com.mny.wan.pkg.utils

import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate

/**
 * 当前是否是夜间模式
 */
fun Context.isNightMode(): Boolean {
    val config = resources.configuration
    val uiMode = config.uiMode and Configuration.UI_MODE_NIGHT_MASK
    return uiMode == Configuration.UI_MODE_NIGHT_YES
}

object ThemeHelper {
    fun setLightMode() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }

    fun setNightMode() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
    }

    fun setSystemMode() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
    }
}

