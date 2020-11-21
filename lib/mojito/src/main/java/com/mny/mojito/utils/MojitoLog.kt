package com.mny.mojito.utils

import android.util.Log

/**
 * 在 AUC 还没初始化的时候使用
 */
object MojitoLog {
    private const val TAG = "Mojito"
    fun d(content: String) {
        Log.d(TAG, "$content")
    }
}