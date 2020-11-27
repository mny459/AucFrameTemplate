package com.mny.wan.pkg.data.repository

import com.mny.wan.pkg.data.remote.model.BaseResponse
import com.mny.wan.pkg.domain.repository.LoginRepository
import com.mny.wan.pkg.data.remote.service.WanService
import com.mny.wan.pkg.base.BaseRepository
import com.mny.wan.pkg.data.remote.model.BeanCoin
import com.mny.wan.pkg.data.remote.model.UserInfoModel
import javax.inject.Inject

/**
 * Desc:
 */
class LoginRepositoryImpl @Inject constructor() : BaseRepository(), LoginRepository {

    override suspend fun login(username: String, password: String): BaseResponse<UserInfoModel> {
        return mRepository.obtainRetrofitService(WanService::class.java)
            .login(username, password)
    }

    override suspend fun register(
        username: String,
        password: String,
        rePassword: String
    ): BaseResponse<UserInfoModel> {
        return mRepository.obtainRetrofitService(WanService::class.java)
            .register(username, password, rePassword)
    }

    override suspend fun logout(): BaseResponse<String> {
        return mRepository.obtainRetrofitService(WanService::class.java)
            .logout()
    }

    override suspend fun fetchCoinInfo(): BaseResponse<BeanCoin> {
        return mRepository.obtainRetrofitService(WanService::class.java)
            .coinInfo()
    }
}