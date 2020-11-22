package com.mny.wan.pkg.app

import android.app.Application
import android.content.Context
import androidx.fragment.app.FragmentManager
import com.mny.wan.pkg.data.remote.service.WanApi
import com.mny.wan.base.delegate.AppLifecycle
import com.mny.wan.di.module.GlobalModuleConfig
import com.mny.wan.di.module.OkHttpConfiguration
import com.mny.wan.integration.ModuleConfig
import com.mny.wan.pkg.data.local.UserInfoManager
import com.mny.wan.pkg.data.remote.http.SSLSocketClient
import com.mny.wan.utils.MojitoLog
import okhttp3.*
import java.security.cert.X509Certificate
import javax.net.ssl.X509TrustManager

class WanConfig : ModuleConfig {
    private val cookieStore = hashMapOf<String, List<Cookie>>()

    override fun applyOptions(context: Context, builder: GlobalModuleConfig.Builder) {
        MojitoLog.d("applyOptions")
        builder.httpUrl(WanApi.API)
            .okHttpConfiguration(object : OkHttpConfiguration {
                override fun config(appContext: Context, target: OkHttpClient.Builder) {

                    target.sslSocketFactory(SSLSocketClient.getSSLSocketFactory(), object :
                        X509TrustManager {
                        override fun checkClientTrusted(
                            chain: Array<out X509Certificate>?,
                            authType: String?
                        ) {
                        }

                        override fun checkServerTrusted(
                            chain: Array<out X509Certificate>?,
                            authType: String?
                        ) {
                        }

                        override fun getAcceptedIssuers(): Array<X509Certificate> =
                            emptyArray<X509Certificate>()
                    })
                        .cookieJar(object : CookieJar {
                            override fun loadForRequest(url: HttpUrl): List<Cookie> {
                                val list = cookieStore[url.host]
                                return if (list.isNullOrEmpty()) {
                                    if (UserInfoManager.isLogin() && UserInfoManager.getTokenPass().isNotEmpty()) {
                                        val cookie = arrayListOf<Cookie>()
                                        val userInfo = UserInfoManager.userInfo()
                                        userInfo?.apply {
                                            cookie.add(
                                                Cookie.Builder()
                                                    .name("loginUserName")
                                                    .value("${userInfo?.username}")
                                                    .domain(WanApi.DOMAIN)
                                                    .build()
                                            )
                                            cookie.add(
                                                Cookie.Builder()
                                                    .name("token_pass")
                                                    .value("${UserInfoManager.getTokenPass()}")
                                                    .domain(WanApi.DOMAIN)
                                                    .build()
                                            )
                                            cookie.add(
                                                Cookie.Builder()
                                                    .name("loginUserName_wanandroid_com")
                                                    .value("${userInfo?.username}")
                                                    .domain(WanApi.DOMAIN)
                                                    .build()
                                            )
                                            cookie.add(
                                                Cookie.Builder()
                                                    .name("token_pass_wanandroid_com")
                                                    .value("${UserInfoManager.getTokenPass()}")
                                                    .domain(WanApi.DOMAIN)
                                                    .build()
                                            )
                                        }

                                        cookie
                                    } else {
                                        emptyList()
                                    }
                                } else list
                            }

                            override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
                                var save = true
                                cookies.forEach {
                                    if (it.name == "token_pass") UserInfoManager.saveTokenPass(it.value)
                                    if (it.name == "JSESSIONID") save = false
                                }
                                if (save) {
                                    cookieStore[url.host] = cookies
                                }
                            }
                        })
                }
            })
    }

    override fun injectAppLifecycle(context: Context, lifecycles: MutableList<AppLifecycle>) {
        MojitoLog.d("injectAppLifecycle")
        lifecycles.add(WanAppLifecycleImpl())
    }

    override fun injectActivityLifecycle(context: Context, lifecycles: MutableList<Application.ActivityLifecycleCallbacks>) {
        MojitoLog.d("injectActivityLifecycle")
    }

    override fun injectFragmentLifecycle(context: Context, lifecycles: MutableList<FragmentManager.FragmentLifecycleCallbacks>) {
        MojitoLog.d("injectFragmentLifecycle")
    }
}