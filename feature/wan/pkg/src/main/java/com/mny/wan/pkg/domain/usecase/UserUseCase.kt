package com.mny.wan.pkg.domain.usecase

import com.mny.wan.pkg.domain.repository.UserRepository
import javax.inject.Inject
import com.mny.wan.http.MojitoResult
import com.mny.wan.pkg.data.local.UserHelper
import com.mny.wan.pkg.data.local.toCollectionEntityList
import com.mny.wan.pkg.data.local.toEntity
import com.mny.wan.pkg.data.remote.model.BeanCoin
import com.mny.wan.pkg.data.remote.model.BeanUserInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import java.lang.Exception

/**
 * Desc:
 */
class UserUseCase @Inject constructor(private val mRepository: UserRepository) {
    
    suspend fun login(username: String, password: String): Flow<MojitoResult<BeanUserInfo>> {
        return flow {
            val response = mRepository.login(username, password)
            if (response.isSuccess()) {
                val userInfo = response.data
                UserHelper.login(userInfo)
                val user = userInfo.toEntity()
                val collections = userInfo.toCollectionEntityList()
                mRepository.saveUser(user,collections)
                val coinResp = mRepository.fetchCoinInfo()
                if (coinResp.isSuccess()) {
                    mRepository.saveCoin(coinResp.data.toEntity())
                }
                emit(MojitoResult.Success(userInfo))
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
    ): Flow<MojitoResult<BeanUserInfo>> {
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

    suspend fun fetchLocalUserInfo(): BeanUserInfo? {
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