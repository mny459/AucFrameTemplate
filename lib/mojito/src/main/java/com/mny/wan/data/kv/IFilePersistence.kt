package com.mny.wan.data.kv

/**
 * K-V 文件持久化 比如 SP 和 MMKV
 */
interface IFilePersistence {
    /**
     * 设置持久化的文件名
     */
    fun name(name: String): IFilePersistence

    // Boolean
    fun put(key: String, value: Boolean): Boolean
    fun getBool(key: String, defaultValue: Boolean): Boolean

    // Int
    fun put(key: String, value: Int): Boolean
    fun getInt(key: String, defaultValue: Int): Int

    // Long
    fun put(key: String, value: Long): Boolean
    fun getLong(key: String, defaultValue: Long): Long

    // Float
    fun put(key: String, value: Float): Boolean
    fun getFloat(key: String, defaultValue: Float): Float

    // Double
    fun put(key: String, value: Double): Boolean
    fun getDouble(key: String, defaultValue: Double): Double

    // String
    fun put(key: String, value: String): Boolean
    fun getString(key: String, defaultValue: String): String

}