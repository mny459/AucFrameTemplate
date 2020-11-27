package com.mny.wan.pkg.domain.repository


import com.mny.wan.pkg.data.remote.model.BaseResponse
import com.mny.wan.pkg.domain.model.UserInfoDomainModel
import com.mny.wan.http.MojitoResult
import com.mny.wan.pkg.data.remote.model.BeanCoin
import com.mny.wan.pkg.data.remote.model.UserInfoModel


/**
 * Desc:
 */
interface LoginRepository {

    suspend fun login(username: String, password: String): BaseResponse<UserInfoModel>

    suspend fun register(
        username: String,
        password: String,
        rePassword: String
    ): BaseResponse<UserInfoModel>

    suspend fun logout(): BaseResponse<String>

    suspend fun fetchCoinInfo(): BaseResponse<BeanCoin>
}