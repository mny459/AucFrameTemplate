package com.mny.wan.pkg.data.repository


import com.mny.wan.pkg.data.remote.model.BaseResponse
import com.mny.login.data.remote.model.toDomainModel
import com.mny.wan.pkg.domain.model.UserInfoDomainModel
import com.mny.wan.pkg.domain.repository.LoginRepository
import com.mny.wan.pkg.data.remote.service.WanService
import com.mny.wan.http.Result
import com.mny.wan.pkg.base.BaseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Desc:
 */
class LoginRepositoryImpl @Inject constructor() : BaseRepository(), LoginRepository {

    override suspend fun login(username: String, password: String): Result<UserInfoDomainModel> {
        return executeRequest<UserInfoDomainModel>(dataBlock = {
            val response = mRepository.obtainRetrofitService(WanService::class.java)
                .login(username, password)
            emit(Result.Success(response.data.toDomainModel()))
        }, errorBlock = {
            emit(Result.Error(Exception("有错误")))
        })
    }

    override suspend fun register(
        username: String,
        password: String,
        rePassword: String
    ): Result<UserInfoDomainModel> {
        val response = withContext(Dispatchers.IO) {
            mRepository.obtainRetrofitService(WanService::class.java)
                .register(username, password, rePassword)
        }
        return resultConvert(response) { it.toDomainModel() }
    }

    override suspend fun logout(): BaseResponse<String> {
        return mRepository.obtainRetrofitService(WanService::class.java)
            .logout()
    }
}