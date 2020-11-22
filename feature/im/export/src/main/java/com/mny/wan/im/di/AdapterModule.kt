package com.mny.wan.im.di

import com.mny.wan.im.domain.repository.UserRepository
import com.mny.wan.im.presentation.search.adapter.SearchUserAdapter
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