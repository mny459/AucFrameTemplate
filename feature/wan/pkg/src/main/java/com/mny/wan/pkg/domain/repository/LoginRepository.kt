package com.mny.wan.pkg.domain.repository


import com.mny.wan.pkg.data.remote.model.BaseResponse
import com.mny.wan.pkg.domain.model.UserInfoDomainModel
import com.mny.wan.http.Result


/**
 * Desc:
 */
interface LoginRepository {

    suspend fun login(username: String, password: String): Result<UserInfoDomainModel>

    suspend fun register(username: String, password: String, rePassword: String): Result<UserInfoDomainModel>

    suspend fun logout(): BaseResponse<String>
}