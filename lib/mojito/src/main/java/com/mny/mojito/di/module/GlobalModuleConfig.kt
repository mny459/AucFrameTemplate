package com.mny.mojito.di.module

import com.mny.mojito.http.log.DefaultFormatPrinter
import com.mny.mojito.http.log.FormatPrinter
import com.mny.mojito.http.log.LogInterceptor
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.Interceptor
import okhttp3.internal.threadFactory
import java.util.concurrent.ExecutorService
import java.util.concurrent.SynchronousQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

/**
 *@author mny on 2020/5/17.
 *        Email：mny9@outlook.com
 *        Desc:
 */
class GlobalModuleConfig internal constructor(builder: Builder) {

    private var mIOExecutorService: ExecutorService = builder.mIOExecutorService
    private var mLogInterceptor: Interceptor = builder.mLogInterceptor
    private var mHttpUrl: HttpUrl = builder.mHttpUrl
    private var mGsonConfiguration: GsonConfiguration? = builder.mGsonConfiguration
    private var mOkHttpConfiguration: OkHttpConfiguration? = builder.mOkHttpConfiguration
    private var mLogLevel: LogInterceptor.Level = builder.mLogLevel
    private var mPrinter: FormatPrinter = builder.mPrinter

    fun builder(): Builder {
        return Builder()
    }

    fun provideIOExecutorService(): ExecutorService = mIOExecutorService
    fun provideLogInterceptor(): Interceptor = mLogInterceptor
    fun provideHttpUrl(): HttpUrl = mHttpUrl

    fun provideGsonConfiguration(): GsonConfiguration? = mGsonConfiguration

    fun provideOkHttpConfiguration(): OkHttpConfiguration? = mOkHttpConfiguration

    fun provideHttpLogLevel(): LogInterceptor.Level = mLogLevel

    fun provideHttpLogPrinter(): FormatPrinter = mPrinter

    class Builder {
        internal var mHttpUrl: HttpUrl = "https://api.github.com".toHttpUrl()
        internal var mIOExecutorService: ExecutorService = ThreadPoolExecutor(0,
                Int.MAX_VALUE,
                60,
                TimeUnit.SECONDS,
                SynchronousQueue(),
                threadFactory("Mojito Executor", false))
        internal var mLogInterceptor: Interceptor = LogInterceptor()

        internal var mGsonConfiguration: GsonConfiguration? = null
        internal var mOkHttpConfiguration: OkHttpConfiguration? = null
        internal var mLogLevel: LogInterceptor.Level = LogInterceptor.Level.ALL
        internal var mPrinter: FormatPrinter = DefaultFormatPrinter()
        fun ioExecutorService(executorService: ExecutorService) = apply {
            this.mIOExecutorService = executorService
        }

        fun logInterceptor(interceptor: Interceptor) = apply {
            this.mLogInterceptor = interceptor
        }

        fun httpUrl(url: String) = apply {
            this.mHttpUrl = url.toHttpUrl()
        }

        fun gsonConfiguration(configuration: GsonConfiguration) = apply {
            this.mGsonConfiguration = configuration
        }

        fun okHttpConfiguration(configuration: OkHttpConfiguration) = apply {
            this.mOkHttpConfiguration = configuration
        }

        fun logLevel(level: LogInterceptor.Level) = apply {
            this.mLogLevel = level
        }

        fun formatPrinter(printer: FormatPrinter) = apply {
            this.mPrinter = printer
        }

        fun build(): GlobalModuleConfig = GlobalModuleConfig(this)
    }
}