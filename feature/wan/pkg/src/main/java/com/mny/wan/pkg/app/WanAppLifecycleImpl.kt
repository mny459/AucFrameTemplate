package com.mny.wan.pkg.app

import android.app.Application
import android.content.Context
import com.blankj.utilcode.util.LogUtils
import com.mny.mojito.base.delegate.AppLifecycle
import com.mny.wan.pkg.data.local.UserHelper
import com.mny.mojito.utils.MojitoLog
import com.mny.wan.pkg.utils.SettingHelper
import com.mny.wan.pkg.utils.ThemeHelper

class WanAppLifecycleImpl : AppLifecycle {
    override fun attachBaseContext(base: Context) {
        MojitoLog.d("DemoAppLifecycleImpl - attachBaseContext")
    }

    override fun onCreate(application: Application) {
        MojitoLog.d("DemoAppLifecycleImpl - onCreate")
        LogUtils.getConfig().globalTag = "Wan"
        UserHelper.initUserInfo()
//        SettingHelper.setThemeDark()
    }

    override fun onTerminate(application: Application) {
        MojitoLog.d("DemoAppLifecycleImpl - onTerminate")
    }
}