package com.mny.wan.im.app

import android.app.Application
import android.content.Context
import android.text.TextUtils
import androidx.fragment.app.FragmentManager
import com.google.gson.GsonBuilder
import com.mny.mojito.base.delegate.AppLifecycle
import com.mny.wan.im.data.api.Api
import com.mny.wan.im.data.factory.AccountManager
import com.mny.wan.di.module.GlobalModuleConfig
import com.mny.wan.di.module.GsonConfiguration
import com.mny.wan.di.module.OkHttpConfiguration
import com.mny.wan.integration.ModuleConfig
import com.mny.mojito.utils.MojitoLog
import okhttp3.Cookie
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response

class DemoConfig : ModuleConfig {
    private val cookieStore = hashMapOf<String, List<Cookie>>()

    companion object {
    }

    override fun applyOptions(context: Context, builder: GlobalModuleConfig.Builder) {
        MojitoLog.d("applyOptions")
        builder.httpUrl(Api.URL)
                .gsonConfiguration(object : GsonConfiguration {
                    override fun config(appContext: Context, target: GsonBuilder) {
                        target.setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
                    }
                })
                .okHttpConfiguration(object : OkHttpConfiguration {
                    override fun config(appContext: Context, target: OkHttpClient.Builder) {
                        target.addInterceptor(object : Interceptor {
                            override fun intercept(chain: Interceptor.Chain): Response {
                                // 拿到我们的请求
                                val original = chain.request()
                                // 重新进行build
                                val requestBuilder = original.newBuilder()
                                if (!TextUtils.isEmpty(AccountManager.getToken())) {
                                    // 注入一个token
                                    requestBuilder.addHeader("token", AccountManager.getToken())
                                }
                                requestBuilder.addHeader("Content-Type", "application/json")
//                                requestBuilder.addHeader("Connection", "close")
                                val newRequest = requestBuilder.build()
                                // 返回
                                return chain.proceed(newRequest)
                            }
                        })
                    }
                })
    }

    override fun injectAppLifecycle(context: Context, lifecycles: MutableList<AppLifecycle>) {
        MojitoLog.d("injectAppLifecycle")
        lifecycles.add(DemoAppLifecycleImpl())
    }

    override fun injectActivityLifecycle(context: Context, lifecycles: MutableList<Application.ActivityLifecycleCallbacks>) {
        MojitoLog.d("injectActivityLifecycle")
    }

    override fun injectFragmentLifecycle(context: Context, lifecycles: MutableList<FragmentManager.FragmentLifecycleCallbacks>) {
        MojitoLog.d("injectFragmentLifecycle")
    }
}