package com.mny.wan.pkg.extension

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.view.View
import androidx.annotation.ColorInt
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.ColorUtils
import com.google.android.material.color.MaterialColors

private const val EDGE_TO_EDGE_BAR_ALPHA = 128
private const val EDGE_TO_EDGE_FLAGS =
    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_LAYOUT_STABLE

/**
 * 设置是否全屏（状态栏和导航栏不占空间）
 */
@SuppressLint("ObsoleteSdkInt")
fun AppCompatActivity.applyEdgeToEdgePreference(edgeToEdgeEnabled: Boolean) {
    // 5.0 以下免谈
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) return
    val statusBarColor = getStatusBarColor(edgeToEdgeEnabled, this)
    val navBarColor = getNavBarColor(edgeToEdgeEnabled, this)

    val lightBackground =
        isColorLight(MaterialColors.getColor(this, android.R.attr.colorBackground, Color.BLACK))
    val lightNavBar = isColorLight(navBarColor)
    val showDarkNavBarIcons =
        lightNavBar || navBarColor == Color.TRANSPARENT && lightBackground

    with(window) {
        val currentStatusBar =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) decorView.systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR else 0
        val currentNavBar =
            if (showDarkNavBarIcons && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR else 0

        this.navigationBarColor = navBarColor
        this.statusBarColor = statusBarColor
        val systemUiVisibility =
            ((if (edgeToEdgeEnabled) EDGE_TO_EDGE_FLAGS else View.SYSTEM_UI_FLAG_VISIBLE)
                    or currentStatusBar
                    or currentNavBar)

        decorView.systemUiVisibility = systemUiVisibility
    }
}

/**
 * 获取状态栏颜色
 */
@SuppressLint("ObsoleteSdkInt")
fun getStatusBarColor(edgeToEdgeEnabled: Boolean, context: Context): Int = when {
    //低版本
    edgeToEdgeEnabled && Build.VERSION.SDK_INT < Build.VERSION_CODES.M -> {
        val opaqueStatusBarColor =
            MaterialColors.getColor(context, android.R.attr.statusBarColor, Color.BLACK)
        ColorUtils.setAlphaComponent(opaqueStatusBarColor, EDGE_TO_EDGE_BAR_ALPHA)
    }
    edgeToEdgeEnabled -> Color.TRANSPARENT
    else -> MaterialColors.getColor(context, android.R.attr.statusBarColor, Color.BLACK)
}

/**
 * 获取导航栏颜色
 */
fun getNavBarColor(edgeToEdgeEnabled: Boolean, context: Context): Int = when {
    //低版本
    edgeToEdgeEnabled && Build.VERSION.SDK_INT < Build.VERSION_CODES.O_MR1 -> {
        val opaqueStatusBarColor =
            MaterialColors.getColor(context, android.R.attr.navigationBarColor, Color.BLACK)
        ColorUtils.setAlphaComponent(opaqueStatusBarColor, EDGE_TO_EDGE_BAR_ALPHA)
    }
    edgeToEdgeEnabled -> Color.TRANSPARENT
    else -> MaterialColors.getColor(context, android.R.attr.navigationBarColor, Color.BLACK)
}

fun isColorLight(@ColorInt color: Int): Boolean {
    return color != Color.TRANSPARENT && ColorUtils.calculateLuminance(color) > 0.5
}