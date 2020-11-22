package com.mny.wan.im.domain.usecase

import androidx.lifecycle.LiveData
import com.mny.wan.im.data.api.model.user.UserUpdateModel
import com.mny.wan.im.domain.model.UserInfoDomainModel
import com.mny.wan.im.domain.repository.UserRepository
import com.mny.wan.http.Result
import com.mny.wan.im.data.card.UserCard
import com.mny.wan.im.data.db.entity.UserEntity
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