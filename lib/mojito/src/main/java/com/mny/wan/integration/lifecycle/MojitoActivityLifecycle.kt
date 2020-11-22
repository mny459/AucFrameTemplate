package com.mny.wan.integration.lifecycle

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.blankj.utilcode.util.LogUtils

/**
 * 目前功能
 * 1. 打印生命周期日志
 */
class MojitoActivityLifecycle : Application.ActivityLifecycleCallbacks {
    override fun onActivityPaused(activity: Activity) {
        LogUtils.v("$activity - onActivityPaused")
    }

    override fun onActivityStarted(activity: Activity) {
        LogUtils.v("$activity - onActivityStarted")

    }

    override fun onActivityDestroyed(activity: Activity) {
        LogUtils.v("$activity - onActivityDestroyed")

    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        LogUtils.v("$activity - onActivitySaveInstanceState - $outState")

    }

    override fun onActivityStopped(activity: Activity) {
        LogUtils.v("$activity - onActivityStopped")

    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        LogUtils.v("$activity - onActivityCreated savedInstanceState - $savedInstanceState")

    }

    override fun onActivityResumed(activity: Activity) {
        LogUtils.v("$activity - onActivityResumed")
    }
}