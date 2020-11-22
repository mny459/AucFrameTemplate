package com.mny.wan.pkg.utils

import com.blankj.utilcode.util.Utils
import com.tencent.mmkv.MMKV

/**
 * @Author CaiRj
 * @Date 2019/8/20 14:28
 * @Desc
 */
object MMKVUtils {
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
            mmkv.reKey(key)
        }
    }

    // Boolean
    fun put(id: String, key: String, value: Boolean): Boolean {
        return getMmkv(id).encode(key, value)
    }

    fun getBool(id: String, key: String, defaultValue: Boolean): Boolean {
        return getMmkv(id).decodeBool(key, defaultValue)
    }

    // Int
    fun put(id: String, key: String, value: Int): Boolean {
        return getMmkv(id).encode(key, value)
    }

    fun getInt(id: String, key: String, defaultValue: Int): Int {
        return getMmkv(id).decodeInt(key, defaultValue)
    }

    // Long
    fun put(id: String, key: String, value: Long): Boolean {
        return getMmkv(id).encode(key, value)
    }

    fun getLong(id: String, key: String, defaultValue: Long): Long {
        return getMmkv(id).decodeLong(key, defaultValue)
    }

    // Float
    fun put(id: String, key: String, value: Float): Boolean {
        return getMmkv(id).encode(key, value)
    }

    fun getFloat(id: String, key: String, defaultValue: Float): Float {
        return getMmkv(id).decodeFloat(key, defaultValue)
    }

    // Double
    fun put(id: String, key: String, value: Double): Boolean {
        return getMmkv(id).encode(key, value)
    }

    fun getDouble(id: String, key: String, defaultValue: Double): Double {
        return getMmkv(id).decodeDouble(key, defaultValue)
    }

    // String
    fun put(id: String, key: String, value: String): Boolean {
        return put(DEFALUT_PATH, id, key, value)
    }

    fun put(path: String, id: String, key: String, value: String): Boolean {
        return getMmkv(id, path).encode(key, value)
    }

    fun getString(id: String, key: String, defaultValue: String): String {
        return getMmkv(id).decodeString(key, defaultValue)
    }

}

//// Boolean
//fun MMKV.put(key: String, value: Boolean): Boolean {
//    return this.encode(key, value)
//}
//
//fun MMKV.getBool(key: String, defaultValue: Boolean): Boolean {
//    return this.decodeBool(key, defaultValue)
//}
//
//// Int
//fun MMKV.put(key: String, value: Int): Boolean {
//    return this.encode(key, value)
//}
//
//fun MMKV.getInt(key: String, defaultValue: Int): Int {
//    return this.decodeInt(key, defaultValue)
//}
//
//// Long
//fun MMKV.put(key: String, value: Long): Boolean {
//    return this.encode(key, value)
//}
//
//fun MMKV.getLong(key: String, defaultValue: Long): Long {
//    return this.decodeLong(key, defaultValue)
//}
//
//// Float
//fun MMKV.put(key: String, value: Float): Boolean {
//    return this.encode(key, value)
//}
//
//fun MMKV.getFloat(key: String, defaultValue: Float): Float {
//    return this.decodeFloat(key, defaultValue)
//}
//
//// Double
//fun MMKV.put(key: String, value: Double): Boolean {
//    return this.encode(key, value)
//}
//
//fun MMKV.getDouble(key: String, defaultValue: Double): Double {
//    return this.decodeDouble(key, defaultValue)
//}
//
//// String
//fun MMKV.put(key: String, value: String): Boolean {
//    return this.encode(key, value)
//}
//
//fun MMKV.getString(key: String, defaultValue: String): String {
//    return this.decodeString(key, defaultValue)
//}
