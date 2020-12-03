package com.mny.wan.base.delegate

import android.app.Application
import android.content.Context
import androidx.fragment.app.FragmentManager
import com.blankj.utilcode.util.CrashUtils
import com.blankj.utilcode.util.LogUtils
import com.mny.wan.di.factory.GlobalConfigFactory
import com.mny.wan.di.module.GlobalModuleConfig
import com.mny.wan.integration.ManifestParser
import com.mny.wan.integration.ModuleConfig
import com.mny.wan.integration.lifecycle.MojitoActivityLifecycle

/**
 *@author mny on 2020/5/17.
 *        Email：mny9@outlook.com
 *        Desc:
 */
class AppDelegate(private val mApplication: Application?,
                  private var mModuleConfigs: MutableList<ModuleConfig> = mutableListOf(),
                  private val mAppLifecycleList: MutableList<AppLifecycle> = mutableListOf(),
                  private val mActivityLifecycleList: MutableList<Application.ActivityLifecycleCallbacks> = mutableListOf(),
                  private val mFragmentLifecycleList: MutableList<FragmentManager.FragmentLifecycleCallbacks> = mutableListOf(),
                  private var mActivityLifecycle: Application.ActivityLifecycleCallbacks? = MojitoActivityLifecycle()
) : AppLifecycle {

    init {
        mApplication?.apply {
            val parsedConfigs = ManifestParser(this).parse()
            mModuleConfigs.addAll(parsedConfigs)
            mModuleConfigs.forEach {
                it.injectAppLifecycle(this, mAppLifecycleList)
                it.injectActivityLifecycle(this, mActivityLifecycleList)
                it.injectFragmentLifecycle(this, mFragmentLifecycleList)
            }
        }
    }

    override fun attachBaseContext(base: Context) {
        mAppLifecycleList.forEach { it.attachBaseContext(base) }
    }

    override fun onCreate(application: Application) {
        // 初始化全局公共配置
        GlobalConfigFactory.saveGlobalModuleConfig(getGlobalModuleConfig(application, mModuleConfigs))
        mActivityLifecycle?.apply {
            mApplication?.registerActivityLifecycleCallbacks(this)
        }
        mActivityLifecycleList.forEach { mApplication?.registerActivityLifecycleCallbacks(it) }
        mAppLifecycleList.forEach { it.onCreate(application) }
        CrashUtils.init { crashInfo ->
            LogUtils.e("$crashInfo")
        }
    }


    private fun getGlobalModuleConfig(context: Context, configs: MutableList<ModuleConfig>): GlobalModuleConfig {
        val builder = GlobalModuleConfig.Builder()
        configs.forEach { it.applyOptions(context, builder) }
        return builder.build()
    }


    override fun onTerminate(application: Application) {
        mActivityLifecycle?.apply {
            mApplication?.unregisterActivityLifecycleCallbacks(this)
        }

        mActivityLifecycleList.forEach {
            mApplication?.unregisterActivityLifecycleCallbacks(it)
        }

        mAppLifecycleList.forEach { it.onTerminate(application) }

        mActivityLifecycle = null
        mActivityLifecycleList.clear()
        mAppLifecycleList.clear()

    }
}