package com.mny.wan.status

/**
 * 状态管理：数据加载中、加载成功、加载出错、加载完成、空
 * Status
 * @author crj
 * Created on 2020-07-23 16:54
 */
sealed class Status<out T : Any> {
    object Empty : Status<Nothing>()
    object Loading : Status<Nothing>()
    data class Success<out T : Any>(val data: T?) : Status<T>()
    data class Error(val exception: Exception) : Status<Nothing>()
    object Complete : Status<Nothing>()
}