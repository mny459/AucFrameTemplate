package com.mny.mojito.im.data.repository

import android.text.TextUtils
import android.util.Log
import com.blankj.utilcode.util.LogUtils
import com.mny.mojito.im.base.BaseRepository
import com.mny.mojito.im.domain.model.UserInfoDomainModel
import com.mny.mojito.im.domain.repository.AccountRepository
import com.mny.mojito.im.data.api.model.account.LoginModel
import com.mny.mojito.im.data.api.model.account.RegisterModel
import com.mny.mojito.im.data.api.model.account.toUserDomainModel
import com.mny.mojito.http.Result
import com.mny.mojito.im.data.api.model.BaseResponse
import com.mny.mojito.im.data.api.model.account.AccountRspModel
import com.mny.mojito.im.data.api.service.UserService
import com.mny.mojito.im.data.db.AppRoomDatabase
import com.mny.mojito.im.data.db.entity.UserEntity
import com.mny.mojito.im.data.factory.AccountManager
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