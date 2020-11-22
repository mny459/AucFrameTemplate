package com.mny.wan.im.data.repository

import com.mny.wan.im.base.BaseRepository
import com.mny.wan.im.domain.model.UserInfoDomainModel
import com.mny.wan.im.domain.repository.AccountRepository
import com.mny.wan.im.data.api.model.account.LoginModel
import com.mny.wan.im.data.api.model.account.RegisterModel
import com.mny.wan.im.data.api.model.account.toUserDomainModel
import com.mny.wan.http.Result
import com.mny.wan.im.data.api.model.BaseResponse
import com.mny.wan.im.data.api.model.account.AccountRspModel
import com.mny.wan.im.data.api.service.UserService
import com.mny.wan.im.data.db.AppRoomDatabase
import com.mny.wan.im.data.factory.AccountManager
import javax.inject.Inject

/**
 * Desc:
 */
class AccountRepositoryImpl @Inject constructor(private val mRoomDatabase: AppRoomDatabase)
    : BaseRepository(), AccountRepository {

    override suspend fun login(phone: String, password: String): BaseResponse<AccountRspModel> {
        return mRepository.obtainRetrofitService(UserService::class.java)
                .accountLogin(LoginModel(phone, password, AccountManager.getPushId()))
    }

    override suspend fun accountRegister(phone: String, username: String, password: String): Result<UserInfoDomainModel> {
        return executeRequest(dataBlock = {
            val response = mRepository.obtainRetrofitService(UserService::class.java)
                    .accountRegister(RegisterModel(phone, password, username, AccountManager.getPushId()))
            emit(resultConvert(response) { it.toUserDomainModel() })
        }, errorBlock = {
            emit(Result.Error(Exception("有错误")))
        })
    }

    override suspend fun accountBindPush(pushId: String): Result<UserInfoDomainModel> {
        return executeRequest(dataBlock = {
            val response = mRepository.obtainRetrofitService(UserService::class.java)
                    .accountBind(pushId)
            emit(resultConvert(response) { it.toUserDomainModel() })
        }, errorBlock = {
            emit(Result.Error(Exception("有错误")))
        })
    }
}