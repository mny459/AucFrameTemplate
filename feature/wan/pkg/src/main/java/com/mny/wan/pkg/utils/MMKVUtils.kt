package com.mny.wan.pkg.utils

import com.blankj.utilcode.util.Utils
import com.mny.mojito.integration.KVHelper
import com.tencent.mmkv.MMKV

/**
 * @Author CaiRj
 * @Date 2019/8/20 14:28
 * @Desc
 */
object MMKVUtils : KVHelper {
    private var mmkvs = hashMapOf<String, MMKV>()
    const val DEFALUT_PATH = "mk"

    init {
        init()
    }

    fun init() {
        MMKV.initialize("/data/data/${Utils.getApp().packageName}/mmkv/")
    }

    fun getMmkv(id: String, path: String = DEFALUT_PATH): MMKV {
        val key = "$id"
        return if (mmkvs.containsKey(key) && mmkvs[key] != null) mmkvs[key]!!
        else {
            val mmkv = MMKV.mmkvWithID(id, "/data/data/${Utils.getApp().packageName}/mmkv/${path}")
                ?: MMKV.mmkvWithID(id)
            mmkvs[key] = mmkv
            mmkv
        }
    }

    fun removeKey(id: String, path: String = DEFALUT_PATH, key: String) {
        val name = "$id"
        if (mmkvs.containsKey(name) && mmkvs[name] != null) mmkvs[name]?.reKey(key)
        else {
            val mmkv = MMKV.mmkvWithID(id, "/data/data/${Utils.getApp().packageName}/mmkv/${path}")
                ?: MMKV.mmkvWithID(id)
            mmkvs[key] = mmkv
            mmkv.removeValueForKey(key)
        }
    }

    // Boolean
    override fun put(key: String, value: Boolean, name: String): Boolean {
        return getMmkv(name).encode(key, value)
    }

    override fun getBool(key: String, defaultValue: Boolean, name: String): Boolean {
        return getMmkv(name).decodeBool(key, defaultValue)
    }

    // Int
    override fun put(key: String, value: Int, name: String): Boolean {
        return getMmkv(name).encode(key, value)
    }

    override fun getInt(key: String, defaultValue: Int, name: String): Int {
        return getMmkv(name).decodeInt(key, defaultValue)
    }

    // Long
    override fun put(key: String, value: Long, name: String): Boolean {
        return getMmkv(name).encode(key, value)
    }

    override fun getLong(key: String, defaultValue: Long, name: String): Long {
        return getMmkv(name).decodeLong(key, defaultValue)
    }

    // Float
    override fun put(key: String, value: Float, name: String): Boolean {
        return getMmkv(name).encode(key, value)
    }

    override fun getFloat(key: String, defaultValue: Float, name: String): Float {
        return getMmkv(name).decodeFloat(key, defaultValue)
    }

    // Double
    override fun put(key: String, value: Double, name: String): Boolean {
        return getMmkv(name).encode(key, value)
    }

    override fun getDouble(key: String, defaultValue: Double, name: String): Double {
        return getMmkv(name).decodeDouble(key, defaultValue)
    }

    // String
    override fun put(key: String, value: String, name: String): Boolean {
        return getMmkv(name).encode(key, value)
    }


    override fun getString(key: String, defaultValue: String, name: String): String {
        return getMmkv(name).decodeString(key, defaultValue)
    }

}