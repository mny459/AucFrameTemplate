package com.mny.wan.pkg.domain.usecase

import com.mny.wan.pkg.data.remote.model.BaseResponse
import com.mny.wan.pkg.domain.model.UserInfoDomainModel
import com.mny.wan.pkg.domain.repository.LoginRepository
import javax.inject.Inject
import com.mny.wan.http.Result
/**
 * Desc:
 */
class LoginUseCase @Inject constructor(private val mRepository: LoginRepository) {

    suspend fun login(username: String, password: String): Result<UserInfoDomainModel> {
        return mRepository.login(username, password)
    }

    suspend fun register(username: String, password: String, rePassword: String): Result<UserInfoDomainModel> {
        return mRepository.register(username, password, rePassword)
    }

    suspend fun logout(): BaseResponse<String> {
        return mRepository.logout()
    }
}