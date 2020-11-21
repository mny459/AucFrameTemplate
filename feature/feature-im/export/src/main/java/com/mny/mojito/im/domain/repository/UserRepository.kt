package com.mny.mojito.im.domain.repository

import androidx.lifecycle.LiveData
import com.mny.mojito.im.data.api.model.user.UserUpdateModel
import com.mny.mojito.im.domain.model.UserInfoDomainModel
import com.mny.mojito.http.Result
import com.mny.mojito.im.data.api.model.BaseResponse
import com.mny.mojito.im.data.card.GroupCard
import com.mny.mojito.im.data.card.UserCard
import com.mny.mojito.im.data.db.entity.UserEntity
import kotlinx.coroutines.flow.Flow

/**
 * Desc:
 */
interface UserRepository {
    suspend fun updateUserInfo(model: UserUpdateModel): Result<UserInfoDomainModel>
    suspend fun followUser(userId: String): Result<UserCard>
    suspend fun findUser(userId: String): Result<UserCard>
    fun getContactsFromLocal(): LiveData<List<UserEntity>>
    suspend fun getContacts(): Result<List<UserCard>>
    suspend fun searchUser(username: String): BaseResponse<List<UserCard>>
    suspend fun searchGroup(groupName: String): BaseResponse<List<GroupCard>>

    /**
     * 通过 ID 搜索 用户，本地 -> 远程
     */
    suspend fun searchUserById(userId: String): Result<UserEntity>
    fun findUserFromLocal(userId: String): LiveData<UserEntity>
    fun findUserById(userId: String): UserEntity
    fun findLocalUserByIdSync(userId: String): UserEntity
}