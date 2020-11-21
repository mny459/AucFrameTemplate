package com.mny.mojito.im.domain.usecase

import androidx.lifecycle.LiveData
import com.mny.mojito.im.data.api.model.user.UserUpdateModel
import com.mny.mojito.im.domain.model.UserInfoDomainModel
import com.mny.mojito.im.domain.repository.UserRepository
import com.mny.mojito.http.Result
import com.mny.mojito.im.data.card.UserCard
import com.mny.mojito.im.data.db.entity.UserEntity
import javax.inject.Inject

/**
 * Desc:
 */
class UserUseCase @Inject constructor(private val mRepository: UserRepository) {
    suspend fun updateUserInfo(model: UserUpdateModel): Result<UserInfoDomainModel> {
        return mRepository.updateUserInfo(model)
    }

    suspend fun findUser(userId: String): Result<UserCard> {
        return mRepository.findUser(userId)
    }

    suspend fun findUserFromLocal(userId: String): LiveData<UserEntity> {
        return mRepository.findUserFromLocal(userId)
    }

}