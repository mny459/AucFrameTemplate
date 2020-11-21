package com.mny.mojito.di.factory

import com.google.gson.Gson
import com.mny.mojito.BuildConfig
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitFactory {

    fun createRetrofit(builder: Retrofit.Builder, client: OkHttpClient, url: HttpUrl, gson: Gson): Retrofit {
        builder.baseUrl(url)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .validateEagerly(BuildConfig.DEBUG)
        return builder.build()
    }

    fun createRetrofitBuilder(): Retrofit.Builder = Retrofit.Builder()
}