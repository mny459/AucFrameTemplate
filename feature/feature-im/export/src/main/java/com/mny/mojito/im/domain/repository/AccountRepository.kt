package com.mny.mojito.im.domain.repository


import com.mny.mojito.im.domain.model.UserInfoDomainModel
import com.mny.mojito.http.Result
import com.mny.mojito.im.data.api.model.BaseResponse
import com.mny.mojito.im.data.api.model.account.AccountRspModel
import com.mny.mojito.im.data.db.entity.UserEntity

/**
 * Desc: 账号相关
 */
interface AccountRepository {

    suspend fun login(phone: String, password: String): BaseResponse<AccountRspModel>

    suspend fun accountRegister(phone: String, username: String, password: String): Result<UserInfoDomainModel>

    suspend fun accountBindPush(pushId: String): Result<UserInfoDomainModel>

}