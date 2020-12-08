package com.mny.wan.pkg.app

import android.app.Application
import android.content.Context
import com.blankj.utilcode.util.LogUtils
import com.mny.mojito.base.delegate.AppLifecycle
import com.mny.wan.pkg.data.local.UserHelper
import com.mny.mojito.utils.MojitoLog

class WanAppLifecycleImpl : AppLifecycle {
    override fun attachBaseContext(base: Context) {
        MojitoLog.d("DemoAppLifecycleImpl - attachBaseContext")
    }

    override fun onCreate(application: Application) {
        MojitoLog.d("DemoAppLifecycleImpl - onCreate")
        LogUtils.getConfig().globalTag = "Wan"
        UserHelper.initUserInfo()
    }

    override fun onTerminate(application: Application) {
        MojitoLog.d("DemoAppLifecycleImpl - onTerminate")
    }
}