package com.mny.wan.im.gt

import android.content.Context
import android.content.pm.PackageManager
import android.util.Log

/**
 * Time：2020-03-10 on 11:37.
 * Decription:.
 * Author:jimlee.
 */
object GTConfig {
    const val AUTH_ACTION = "com.action.auth"
    private val TAG = GTConfig::class.java.simpleName
    var appid: String? = ""
    var appsecret: String? = ""
    var appkey: String? = ""
    var appName = ""
    var packageName = ""
    var authToken: String? = null

    fun init(context: Context) {
        parseManifests(context)
    }

    private fun parseManifests(context: Context) {
        packageName = context.packageName
        try {
            val appInfo = context.packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA)
            appName = appInfo.loadLabel(context.packageManager).toString()
            if (appInfo.metaData != null) {
                appid = appInfo.metaData.getString("PUSH_APPID")
                appsecret = appInfo.metaData.getString("PUSH_APPSECRET")
                appkey = appInfo.metaData.getString("PUSH_APPKEY")
            }
        } catch (e: Exception) {
            Log.i(TAG, "parse manifest failed = $e")
        }
    }
}