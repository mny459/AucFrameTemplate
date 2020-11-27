package com.mny.wan.integration

interface KVHelper {
    companion object {
        /**
         * 通用的可以不用自定义名字，和用户相关的可以用用户 id 的md5 值来做 name
         */
        const val DEFAULT_KV_NAME = "mojito"
    }

    // Boolean
    fun put(key: String, value: Boolean, name: String = DEFAULT_KV_NAME): Boolean
    fun getBool(key: String, defaultValue: Boolean, name: String = DEFAULT_KV_NAME): Boolean

    // Int
    fun put(key: String, value: Int, name: String = DEFAULT_KV_NAME): Boolean
    fun getInt(key: String, defaultValue: Int, name: String = DEFAULT_KV_NAME): Int

    // Long
    fun put(key: String, value: Long, name: String = DEFAULT_KV_NAME): Boolean
    fun getLong(key: String, defaultValue: Long, name: String = DEFAULT_KV_NAME): Long

    // Float
    fun put(key: String, value: Float, name: String = DEFAULT_KV_NAME): Boolean
    fun getFloat(key: String, defaultValue: Float, name: String = DEFAULT_KV_NAME): Float

    // Double
    fun put(key: String, value: Double, name: String = DEFAULT_KV_NAME): Boolean
    fun getDouble(key: String, defaultValue: Double, name: String = DEFAULT_KV_NAME): Double

    // String
    fun put(key: String, value: String, name: String = DEFAULT_KV_NAME): Boolean
    fun getString(key: String, defaultValue: String, name: String = DEFAULT_KV_NAME): String
}