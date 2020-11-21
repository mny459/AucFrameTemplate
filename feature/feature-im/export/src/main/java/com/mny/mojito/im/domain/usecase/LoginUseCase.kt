package com.mny.mojito.im.domain.usecase

import android.text.TextUtils
import com.google.gson.Gson
import com.mny.mojito.im.domain.model.UserInfoDomainModel
import com.mny.mojito.im.domain.repository.AccountRepository
import com.mny.mojito.http.Result
import com.mny.mojito.im.data.db.entity.UserEntity
import com.mny.mojito.im.data.factory.AccountManager
import com.mny.mojito.im.domain.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import javax.inject.Inject

/**
 * Desc:
 */
class LoginUseCase @Inject constructor(private val mRepository: AccountRepository,
                                       private val mGson: Gson
) {
    suspend fun login(phone: String, password: String): Flow<Result<UserEntity>> {
        return flow<Result<UserEntity>> {
            val response = mRepository.login(phone, password)
            if (response.isSuccess()) {
                AccountManager.saveUserInfo(response.result, mGson.toJson(response.result))
                if (response.result.isBind) {
                    AccountManager.setBind(true)
                } else {
                    val pushId = AccountManager.getPushId()
                    if (!TextUtils.isEmpty(pushId)) {
                        // TODO 重试发送
                        mRepository.accountBindPush(pushId)
                    }
                }
                emit(Result.Success(response.result.user))
            } else {
                emit(Result.Error(Exception("${response.message}")))
            }
        }.onStart { emit(Result.Loading) }
                .flowOn(Dispatchers.IO)
                .catch { error ->
                    emit(Result.Error(Exception("${error.message}")))
                }

    }

    suspend fun register(phone: String, username: String, password: String): Result<UserInfoDomainModel> {
        return mRepository.accountRegister(phone, username, password)
    }
}