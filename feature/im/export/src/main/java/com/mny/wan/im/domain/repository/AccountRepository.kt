package com.mny.wan.im.domain.repository


import com.mny.wan.im.domain.model.UserInfoDomainModel
import com.mny.wan.http.Result
import com.mny.wan.im.data.api.model.BaseResponse
import com.mny.wan.im.data.api.model.account.AccountRspModel

/**
 * Desc: 账号相关
 */
interface AccountRepository {

    suspend fun login(phone: String, password: String): BaseResponse<AccountRspModel>

    suspend fun accountRegister(phone: String, username: String, password: String): Result<UserInfoDomainModel>

    suspend fun accountBindPush(pushId: String): Result<UserInfoDomainModel>

}