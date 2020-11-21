package com.mny.mojito.im.di

import com.mny.mojito.im.domain.repository.UserRepository
import com.mny.mojito.im.presentation.search.adapter.SearchUserAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@InstallIn(ActivityComponent::class)
@Module
object AdapterModule {
    @Provides
    fun provideSearchUserAdapter(userRepository: UserRepository): SearchUserAdapter = SearchUserAdapter(userRepository)
}