package com.mny.mojito.im.di

import com.mny.mojito.im.data.repository.*
import com.mny.mojito.im.domain.repository.AccountRepository
import com.mny.mojito.im.domain.repository.GroupRepository
import com.mny.mojito.im.domain.repository.MessageRepository
import com.mny.mojito.im.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
abstract class RepositoryModule {
    @Singleton
    @Binds
    abstract fun bindLoginRepository(repository: AccountRepositoryImpl): AccountRepository

    @Singleton
    @Binds
    abstract fun bindUserRepository(repository: UserRepositoryImpl): UserRepository

    @Singleton
    @Binds
    abstract fun bindMessageRepository(repository: MessageRepositoryImpl): MessageRepository

    @Singleton
    @Binds
    abstract fun bindSessionRepository(repository: SessionRepositoryImpl): SessionRepository

    @Singleton
    @Binds
    abstract fun bindGroupRepository(repository: GroupRepositoryImpl): GroupRepository
}