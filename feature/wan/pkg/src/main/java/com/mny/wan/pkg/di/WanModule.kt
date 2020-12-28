package com.mny.wan.pkg.di

import android.content.Context
import androidx.paging.PagingConfig
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


    /**
     * 其他的使用 @Provides
     */
    companion object {
        @JvmStatic
        @Provides
        @Singleton
        fun providePokemonService(retrofit: Retrofit): WanService {
            return retrofit.create(WanService::class.java)
        }

        @JvmStatic
        @Provides
        @Singleton
        fun provideWanDatabase(@ApplicationContext context: Context): WanDataBase {
            return WanDataBase.getDatabase(context)
        }

        @JvmStatic
        @Provides
        @Singleton
        fun provideUserDao(dataBase: WanDataBase): UserDao {
            return dataBase.userDao()
        }

        @JvmStatic
        @Provides
        @Singleton
        fun provideCommonPageConfig(): PagingConfig {
            return PagingConfig(
                pageSize = 20, // 每页显示的数据的大小。对应 PagingSource 里 LoadParams.loadSize
                prefetchDistance = 4, // 预刷新的距离，距离最后一个 item 多远时加载数据
                initialLoadSize = 20,  // 初始化加载数量，默认为 pageSize * 3
                maxSize = 200 // 一次应在内存中保存的最大数据
            )
        }
    }
}
