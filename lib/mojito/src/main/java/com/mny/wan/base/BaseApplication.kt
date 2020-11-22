package com.mny.wan.base

import android.app.Application
import android.content.Context
import com.mny.wan.base.delegate.AppDelegate
import com.mny.wan.base.delegate.AppLifecycle

/**
 *@author mny on 2020/5/17.
 *        Email：mny9@outlook.com
 *        Desc:
 */
abstract class BaseApplication : Application() {
    private lateinit var mAppDelegate: AppLifecycle
    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        mAppDelegate = AppDelegate(this)
    }

    override fun onCreate() {
        super.onCreate()
        mAppDelegate.onCreate(this)
    }

    override fun onTerminate() {
        super.onTerminate()
        mAppDelegate.onTerminate(this)
    }
}