package com.mny.wan.pkg.domain.usecase

import com.mny.wan.pkg.domain.repository.LoginRepository
import javax.inject.Inject
import com.mny.wan.http.MojitoResult
import com.mny.wan.pkg.data.local.UserHelper
import com.mny.wan.pkg.data.remote.model.BeanCoin
import com.mny.wan.pkg.data.remote.model.UserInfoModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import java.lang.Exception

/**
 * Desc:
 */
class UserUseCase @Inject constructor(private val mRepository: LoginRepository) {

    suspend fun login(username: String, password: String): Flow<MojitoResult<UserInfoModel>> {
        return flow {
            val response = mRepository.login(username, password)
            UserHelper.login(response.data)
            if (response.isSuccess()) {
                emit(MojitoResult.Success(response.data))
            } else {
                emit(MojitoResult.Error(Exception(response.errorMsg)))
            }
        }.onStart {
            emit(MojitoResult.Loading)
        }
            .catch { error ->
                emit(MojitoResult.Error(Exception(error)))
            }.flowOn(Dispatchers.IO)
    }

    suspend fun register(
        username: String,
        password: String,
        rePassword: String
    ): Flow<MojitoResult<UserInfoModel>> {
        return flow {
            val response = mRepository.register(username, password, rePassword)
            if (response.isSuccess()) {
                UserHelper.loginOut()
                emit(MojitoResult.Success(response.data))
            } else {
                emit(MojitoResult.Error(Exception(response.errorMsg)))
            }
        }.onStart {
            emit(MojitoResult.Loading)
        }
            .catch { error ->
                emit(MojitoResult.Error(Exception(error)))
            }.flowOn(Dispatchers.IO)
    }

    suspend fun logout(): Flow<MojitoResult<String>> {
        return flow {
            val response = mRepository.logout()
            if (response.isSuccess()) {
                emit(MojitoResult.Success(response.data))
            } else {
                emit(MojitoResult.Error(Exception(response.errorMsg)))
            }
        }.onStart {
            emit(MojitoResult.Loading)
        }
            .catch { error ->
                emit(MojitoResult.Error(Exception(error)))
            }.flowOn(Dispatchers.IO)
    }

    suspend fun fetchLocalUserInfo(): UserInfoModel? {
        return UserHelper.userInfo()
    }

    suspend fun fetchCoinInfo(): Flow<MojitoResult<BeanCoin>> {
        return flow {
            val response = mRepository.fetchCoinInfo()
            if (response.isSuccess()) {
                emit(MojitoResult.Success(response.data))
            } else {
                emit(MojitoResult.Error(Exception(response.errorMsg)))
            }
        }.onStart {
            emit(MojitoResult.Loading)
        }
            .catch { error ->
                emit(MojitoResult.Error(Exception(error)))
            }.flowOn(Dispatchers.IO)
    }
}