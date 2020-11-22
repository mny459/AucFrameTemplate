package com.mny.wan.pkg.di

import com.mny.wan.pkg.data.repository.LoginRepositoryImpl
import com.mny.wan.pkg.data.repository.WanRepositoryImpl
import com.mny.wan.pkg.domain.repository.LoginRepository
import com.mny.wan.pkg.domain.repository.WanRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

/**
 * Desc:
 */
@InstallIn(ActivityComponent::class)
@Module
abstract class WanModule {
    @Binds
    abstract fun bindLoginRepository(repository: LoginRepositoryImpl): LoginRepository

    @Binds
    abstract fun bindWanRepository(repository: WanRepositoryImpl): WanRepository
}