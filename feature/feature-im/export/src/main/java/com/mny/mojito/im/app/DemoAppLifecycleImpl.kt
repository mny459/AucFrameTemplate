package com.mny.mojito.im.app

import android.app.Application
import android.content.Context
import com.blankj.utilcode.util.LogUtils
import com.igexin.sdk.PushManager
import com.mny.mojito.base.delegate.AppLifecycle
import com.mny.mojito.im.data.factory.AccountManager
import com.mny.mojito.im.gt.GTConfig
import com.mny.mojito.utils.MojitoLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DemoAppLifecycleImpl : AppLifecycle {
    override fun attachBaseContext(base: Context) {
        MojitoLog.d("DemoAppLifecycleImpl - attachBaseContext")
    }

    override fun onCreate(application: Application) {
        MojitoLog.d("DemoAppLifecycleImpl - onCreate")
        LogUtils.getConfig().globalTag = "Mojito"
        GlobalScope.launch {
            withContext(Dispatchers.Default) {

            }
            withContext(Dispatchers.Default) {
                GTConfig.init(application)
                PushManager.getInstance().setDebugLogger(application) {
                    LogUtils.i("PUSH_LOG $it")
                }
                PushManager.getInstance().initialize(application)
                AccountManager.load()
            }
        }
    }

    override fun onTerminate(application: Application) {
        MojitoLog.d("DemoAppLifecycleImpl - onTerminate")
    }
}