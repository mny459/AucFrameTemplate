package com.mny.mojito.di.factory

import okhttp3.Dispatcher
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import java.util.concurrent.ExecutorService
import java.util.concurrent.TimeUnit

object OkHttpFactory {
    private const val TIME_OUT = 15L

    fun createOkHttpBuilder(): OkHttpClient.Builder = OkHttpClient.Builder()

    fun createOkHttp(builder: OkHttpClient.Builder, interceptor: Interceptor, executorService: ExecutorService): OkHttpClient {

        builder.connectTimeout(TIME_OUT, TimeUnit.SECONDS)
                .readTimeout(TIME_OUT, TimeUnit.SECONDS)
                .addNetworkInterceptor(interceptor)
                .dispatcher(Dispatcher(executorService))

        return builder.build()
    }
}