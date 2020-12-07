package com.mny.wan.pkg.di

import android.content.Context
import com.mny.wan.pkg.data.local.WanDataBase
import com.mny.wan.pkg.data.local.dao.UserDao
import com.mny.wan.pkg.data.remote.service.WanService
import com.mny.wan.pkg.data.repository.UserRepositoryImpl
import com.mny.wan.pkg.data.repository.WanRepositoryImpl
import com.mny.wan.pkg.domain.repository.UserRepository
import com.mny.wan.pkg.domain.repository.WanRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * Desc: 抽象类的使用 @Binds
 */
@InstallIn(ApplicationComponent::class)
@Module
abstract class WanModule {
    @Singleton
    @Binds
    abstract fun bindLoginRepository(repository: UserRepositoryImpl): UserRepository

    @Singleton
    @Binds
    abstract fun bindWanRepository(repository: WanRepositoryImpl): WanRepository
}

/**
 * 其他的使用 @Provides
 */
@InstallIn(ApplicationComponent::class)
@Module
object WanDataModule {
    @Provides
    @Singleton
    fun providePokemonService(retrofit: Retrofit): WanService {
        return retrofit.create(WanService::class.java)
    }

    @Provides
    @Singleton
    fun provideWanDatabase(@ApplicationContext context: Context): WanDataBase {
        return WanDataBase.getDatabase(context)
    }

    @Provides
    @Singleton
    fun provideUserDao(dataBase: WanDataBase): UserDao {
        return dataBase.userDao()
    }
}