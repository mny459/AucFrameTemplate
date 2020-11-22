package com.mny.wan.im.utils

import java.text.SimpleDateFormat
import java.util.*

/**
 * 时间工具类
 *
 * @author qiujuer Email:qiujuer@live.cn
 * @version 1.0.0
 */
object DateTimeUtil {
    private val FORMAT = SimpleDateFormat("yy-MM-dd", Locale.ENGLISH)

    /**
     * 获取一个简单的时间字符串
     *
     * @param date Date
     * @return 时间字符串
     */
    fun getSampleDate(date: Date): String {
        return FORMAT.format(date)
    }
}