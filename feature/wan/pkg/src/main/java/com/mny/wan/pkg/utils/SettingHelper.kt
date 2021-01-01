package com.mny.wan.pkg.utils

import androidx.appcompat.app.AppCompatDelegate

object SettingHelper {
    val mmkv = MMKVUtils.getMmkv("settings")
    private const val KEY_THEME_MODE = "theme_model"

    fun curTheme() {

    }

    fun setThemeDark() {
        mmkv.putInt(KEY_THEME_MODE, AppCompatDelegate.MODE_NIGHT_YES)
        ThemeHelper.setNightMode()
    }

    fun setThemeLight() {
        mmkv.putInt(KEY_THEME_MODE, AppCompatDelegate.MODE_NIGHT_NO)
        ThemeHelper.setLightMode()
    }

    fun setThemeFollowSystem() {
        mmkv.putInt(KEY_THEME_MODE, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        ThemeHelper.setSystemMode()
    }

    fun setThemeFollowAppSetting() {
        mmkv.putInt(KEY_THEME_MODE, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        ThemeHelper.setSystemMode()
    }

    private fun getCurrentTheme() = mmkv.getInt(
        KEY_THEME_MODE,
        AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
    )

    fun isThemeFollowSystem() = getCurrentTheme() == AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM

    fun isThemeDark() = getCurrentTheme() == AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
    fun initTheme() {
        when {
            isThemeFollowSystem() -> {
                ThemeHelper.setSystemMode()
            }
            isThemeDark() -> {
                ThemeHelper.setNightMode()
            }
            else -> {
                ThemeHelper.setLightMode()
            }
        }
    }
}