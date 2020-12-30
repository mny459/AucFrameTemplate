package com.mny.wan.pkg.data.repository

import com.mny.wan.pkg.data.remote.model.BaseResponse
import com.mny.wan.pkg.domain.repository.UserRepository
import com.mny.wan.pkg.data.remote.service.WanService
import com.mny.wan.pkg.base.BaseRepository
import com.mny.wan.pkg.data.local.dao.UserDao
import com.mny.wan.pkg.data.local.entity.CoinEntity
import com.mny.wan.pkg.data.local.entity.CollectionEntity
import com.mny.wan.pkg.data.local.entity.UserEntity
import com.mny.wan.pkg.data.remote.model.BeanCoin
import com.mny.wan.pkg.data.remote.model.BeanUserInfo
import javax.inject.Inject

/**
 * Desc:
 */
class UserRepositoryImpl @Inject constructor(private val userDao: UserDao) : BaseRepository(),
    UserRepository {

    override suspend fun login(username: String, password: String): BaseResponse<BeanUserInfo> {
        return mRepository.obtainRetrofitService(WanService::class.java)
            .login(username, password)
    }

    override suspend fun register(
        username: String,
        password: String,
        rePassword: String
    ): BaseResponse<BeanUserInfo> {
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

    override fun saveUser(user: UserEntity, collections: List<CollectionEntity>) {
        userDao.insertUser(user, collections)
    }

    override fun saveCoin(coinEntity: CoinEntity) {
        userDao.insertCoin(coinEntity)
    }
}