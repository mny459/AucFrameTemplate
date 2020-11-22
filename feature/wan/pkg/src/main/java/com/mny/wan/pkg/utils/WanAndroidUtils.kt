package com.mny.wan.pkg.utils

import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.TimeUtils
import java.util.concurrent.TimeUnit

/**
 * @Author CaiRj
 * @Date 2019/10/17 16:34
 * @Desc
 */
object WanAndroidUtils {
    fun getTime(time: Long): String {
        val timeSpace = System.currentTimeMillis() - time
        LogUtils.d("${System.currentTimeMillis()}  ${time} ${timeSpace}")
        return when {
            timeSpace < TimeUnit.HOURS.toMillis(1) -> "刚刚"
            timeSpace < TimeUnit.HOURS.toMillis(24) -> "${TimeUnit.MILLISECONDS.toHours(timeSpace)}小时前"
            timeSpace < TimeUnit.HOURS.toMillis(48) -> "1天前"
            timeSpace < TimeUnit.HOURS.toMillis(48) -> "2天前"
            else -> TimeUtils.millis2String(time)
        }
    }
}