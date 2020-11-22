package com.mny.wan.di.module

import android.content.Context
import androidx.annotation.Nullable
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.mny.wan.data.IRepository
import com.mny.wan.data.RepositoryManager
import com.mny.wan.di.factory.GlobalConfigFactory
import com.mny.wan.di.factory.OkHttpFactory
import com.mny.wan.di.factory.RetrofitFactory
import com.mny.wan.http.log.FormatPrinter
import com.mny.wan.http.log.LogInterceptor
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.util.concurrent.ExecutorService
import javax.inject.Singleton

/**
 *@author mny on 2020/5/17.
 *        Email：mny9@outlook.com
 *        Desc: 生命周期和Application一样
 */
@InstallIn(ApplicationComponent::class)
@Module
object AppModule {

    @Singleton
    @Provides
    fun provideRetrofitBuilder(): Retrofit.Builder = RetrofitFactory.createRetrofitBuilder()

    @Singleton
    @Provides
    fun provideOkHttpBuilder(): OkHttpClient.Builder = OkHttpFactory.createOkHttpBuilder()

    @Singleton
    @Provides
    fun provideGson(@ApplicationContext context: Context, config: GsonConfiguration?): Gson {
        val builder = GsonBuilder()
        config?.config(appContext = context, target = builder)
        return builder.create()
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(@ApplicationContext context: Context, builder: OkHttpClient.Builder, config: OkHttpConfiguration?, interceptor: Interceptor, executorService: ExecutorService): OkHttpClient {
        config?.config(appContext = context, target = builder)
        return OkHttpFactory.createOkHttp(builder = builder, interceptor = interceptor, executorService = executorService)
    }

    @Singleton
    @Provides
    fun provideRetrofit(@ApplicationContext context: Context, builder: Retrofit.Builder, client: OkHttpClient, url: HttpUrl, gson: Gson): Retrofit {
        return RetrofitFactory.createRetrofit(builder = builder, client = client, url = url, gson = gson)
    }

    @Singleton
    @Provides
    fun provideGlobalConfig(): GlobalModuleConfig {
        return GlobalConfigFactory.retrieveGlobalConfigModule()
    }


    @Singleton
    @Provides
    fun provideIOExecutorService(config: GlobalModuleConfig): ExecutorService = config.provideIOExecutorService()

//    @Singleton
//    @Provides
//    fun provideLogInterceptor(config: GlobalModuleConfig): Interceptor = config.provideLogInterceptor()

    @Singleton
    @Provides
    fun provideHttpUrl(config: GlobalModuleConfig): HttpUrl = config.provideHttpUrl()

    @Nullable
    @Singleton
    @Provides
    fun provideGsonConfiguration(config: GlobalModuleConfig): GsonConfiguration? = config.provideGsonConfiguration()

    @Singleton
    @Provides
    fun provideOkHttpConfiguration(config: GlobalModuleConfig): OkHttpConfiguration? = config.provideOkHttpConfiguration()

    @Singleton
    @Provides
    fun provideHttpLogLevel(config: GlobalModuleConfig): LogInterceptor.Level = config.provideHttpLogLevel()

    @Singleton
    @Provides
    fun provideHttpLogPrinter(config: GlobalModuleConfig): FormatPrinter = config.provideHttpLogPrinter()

}

@InstallIn(ApplicationComponent::class)
@Module
abstract class AppBindModule {
    @Singleton
    @Binds
    abstract fun bindRepository(repository: RepositoryManager): IRepository

    @Singleton
    @Binds
    abstract fun bindInterceptor(interceptor: LogInterceptor): Interceptor
}