package com.mny.mojito.integration

import android.app.Application
import android.content.Context
import androidx.fragment.app.FragmentManager
import com.mny.mojito.base.delegate.AppLifecycle
import com.mny.mojito.di.module.GlobalModuleConfig

interface ModuleConfig {
    /**
     * 使用 [GlobalModuleConfig.Builder] 给框架配置一些配置参数
     *
     * @param context [Context]
     * @param builder [GlobalModuleConfig.Builder]
     */
    fun applyOptions(context: Context, builder: GlobalModuleConfig.Builder)

    /**
     * 使用 [AppLifecycle] 在 [Application] 的生命周期中注入一些操作
     *
     * @param context    [Context]
     * @param lifecycles [Application] 的生命周期容器, 可向框架中添加多个 [Application] 的生命周期类
     */
    fun injectAppLifecycle(context: Context, lifecycles: MutableList<AppLifecycle>)

    /**
     * 使用 [Application.ActivityLifecycleCallbacks] 在 [Activity] 的生命周期中注入一些操作
     *
     * @param context    [Context]
     * @param lifecycles [Activity] 的生命周期容器, 可向框架中添加多个 [Activity] 的生命周期类
     */
    fun injectActivityLifecycle(context: Context, lifecycles: MutableList<Application.ActivityLifecycleCallbacks>)

    /**
     * 使用 [FragmentManager.FragmentLifecycleCallbacks] 在 [Fragment] 的生命周期中注入一些操作
     *
     * @param context    [Context]
     * @param lifecycles [Fragment] 的生命周期容器, 可向框架中添加多个 [Fragment] 的生命周期类
     */
    fun injectFragmentLifecycle(context: Context, lifecycles: MutableList<FragmentManager.FragmentLifecycleCallbacks>)
}