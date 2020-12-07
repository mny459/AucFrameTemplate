package com.mny.wan.pkg.domain.repository


import com.mny.wan.pkg.data.local.entity.CoinEntity
import com.mny.wan.pkg.data.local.entity.CollectionEntity
import com.mny.wan.pkg.data.local.entity.UserEntity
import com.mny.wan.pkg.data.remote.model.BaseResponse
import com.mny.wan.pkg.data.remote.model.BeanCoin
import com.mny.wan.pkg.data.remote.model.BeanUserInfo


/**
 * Desc:
 */
interface UserRepository {

    suspend fun login(username: String, password: String): BaseResponse<BeanUserInfo>

    suspend fun register(
        username: String,
        password: String,
        rePassword: String
    ): BaseResponse<BeanUserInfo>

    suspend fun logout(): BaseResponse<String>

    suspend fun fetchCoinInfo(): BaseResponse<BeanCoin>
    fun saveUser(user: UserEntity, collections: List<CollectionEntity>)
    fun saveCoin(coinEntity: CoinEntity)
}