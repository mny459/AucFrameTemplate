package com.mny.mojito.im.domain.usecase

import androidx.annotation.WorkerThread
import com.mny.mojito.http.Result
import com.mny.mojito.im.data.card.UserCard
import com.mny.mojito.im.domain.repository.UserRepository
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewComponent
import javax.inject.Inject

@InstallIn(ViewComponent::class)
@EntryPoint
interface FollowUseCaseEntryPoint {
    fun getUseCase(): FollowUseCase
}

class FollowUseCase @Inject constructor(private val mRepository: UserRepository) {
    @WorkerThread
    suspend fun followUser(userId: String): Result<UserCard> {
        return mRepository.followUser(userId)
    }
}