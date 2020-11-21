package com.mny.mojito.im.di

import android.content.Context
import com.mny.mojito.im.data.db.AppRoomDatabase
import com.mny.mojito.im.data.db.dao.*
import com.mny.mojito.im.domain.usecase.SessionUseCase
import com.mny.mojito.im.data.repository.SessionRepository
import com.mny.mojito.im.domain.repository.GroupRepository
import com.mny.mojito.im.domain.repository.MessageRepository
import com.mny.mojito.im.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
object DbModule {

    @Singleton
    @Provides
    fun provideSessionUseCase(
            sessionRepository: SessionRepository,
            messageRepository: MessageRepository,
            userRepository: UserRepository,
            groupRepository: GroupRepository
    ): SessionUseCase = SessionUseCase(
            sessionRepository, messageRepository,
            userRepository,
            groupRepository
    )

    @Singleton
    @Provides
    fun provideAppRoomDatabase(@ApplicationContext context: Context): AppRoomDatabase = AppRoomDatabase.getDatabase(context)

    @Singleton
    @Provides
    fun provideUserDao(database: AppRoomDatabase): UserDao = database.userDao()

    @Singleton
    @Provides
    fun provideGroupDao(database: AppRoomDatabase): GroupDao = database.groupDao()

    @Singleton
    @Provides
    fun provideGroupMemberDao(database: AppRoomDatabase): GroupMemberDao = database.groupMemberDao()

    @Singleton
    @Provides
    fun provideMessageDao(database: AppRoomDatabase): MessageDao = database.messageDao()

    @Singleton
    @Provides
    fun provideSessionDao(database: AppRoomDatabase): SessionDao = database.sessionDao()
}