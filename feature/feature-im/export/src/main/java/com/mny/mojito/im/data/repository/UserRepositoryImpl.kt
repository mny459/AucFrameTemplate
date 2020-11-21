package com.mny.mojito.im.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.mny.mojito.http.Result
import com.mny.mojito.im.base.BaseRepository
import com.mny.mojito.im.data.api.model.BaseResponse
import com.mny.mojito.im.data.api.model.user.UserUpdateModel
import com.mny.mojito.im.data.api.service.UserService
import com.mny.mojito.im.data.card.GroupCard
import com.mny.mojito.im.data.card.UserCard
import com.mny.mojito.im.data.db.AppRoomDatabase
import com.mny.mojito.im.data.db.dao.UserDao
import com.mny.mojito.im.data.db.entity.UserEntity
import com.mny.mojito.im.domain.model.UserInfoDomainModel
import com.mny.mojito.im.domain.repository.UserRepository
import javax.inject.Inject

/**
 * Desc:
 */
class UserRepositoryImpl @Inject constructor(
        private val mDatabase: AppRoomDatabase,
        private val mUserDao: UserDao) : BaseRepository(), UserRepository {
    override suspend fun updateUserInfo(model: UserUpdateModel): Result<UserInfoDomainModel> {
        return executeRequest(dataBlock = {
            val response = mRepository.obtainRetrofitService(UserService::class.java)
                    .userUpdate(model)
            if (response.isSuccess()) {
                mDatabase.userDao().insert(response.result.buildUser())
//                mDbHelper.save(User::class.java, response.result.build())
            }
            emit(resultConvert(response) { it.toUserDomainModel() })
        }, errorBlock = {
            emit(Result.Error(Exception("有错误")))
        })
    }

    override suspend fun followUser(userId: String): Result<UserCard> {
        return executeRequest(dataBlock = {
            val response = mRepository.obtainRetrofitService(UserService::class.java)
                    .userFollow(userId)
            emit(Result.Success(response.result))
        }, errorBlock = {
            emit(Result.Error(Exception("有错误")))
        })
    }

    override suspend fun findUser(userId: String): Result<UserCard> {
        return executeRequest(dataBlock = {
            val response = mRepository.obtainRetrofitService(UserService::class.java)
                    .userFind(userId)
            emit(Result.Success(response.result))
        }, errorBlock = {
            emit(Result.Error(Exception("有错误")))
        })
    }

    override fun getContactsFromLocal(): LiveData<List<UserEntity>> {
        return mDatabase.userDao().queryContacts()
    }

    override suspend fun getContacts(): Result<List<UserCard>> {
        return executeRequest(dataBlock = {
            val response = mRepository.obtainRetrofitService(UserService::class.java)
                    .userContacts()
            if (response.isSuccess()) {
                try {
                    val users = response.result.map { it.buildUser() }
                    mDatabase.userDao().insert(*users.toTypedArray())
                } catch (e: Exception) {
                    Log.e("TAG", "getContacts: ", e)
                }
                emit(Result.Success(response.result))
            } else {
                emit(Result.Error(Exception("${response.message}")))
            }
        }, errorBlock = {
            emit(Result.Error(Exception("有错误")))
        })
    }

    override suspend fun searchUser(username: String): BaseResponse<List<UserCard>> {
        return mRepository.obtainRetrofitService(UserService::class.java)
                .userSearch(username)
    }

    override suspend fun searchGroup(groupName: String): BaseResponse<List<GroupCard> >{
        return mRepository.obtainRetrofitService(UserService::class.java)
                .groupSearch(groupName)
    }
    override suspend fun searchUserById(userId: String): Result<UserEntity> {
        val userFromLocal = findUserFromLocal(userId)
        if (userFromLocal.value != null) {
            return Result.Success(userFromLocal.value)
        }
        return findFromNet(userId)
    }

    override fun findUserById(userId: String): UserEntity {
        return findLocalUserByIdSync(userId)
    }

    override fun findLocalUserByIdSync(userId: String): UserEntity {
        return mUserDao.findUserByIdSync(userId)
    }

    override fun findUserFromLocal(userId: String): LiveData<UserEntity> = mDatabase.userDao().queryUserById(userId)


//    private fun findUserFromServer(userId: String): Flow<UserEntity> {
//        val response = mRepository.obtainRetrofitService(UserService::class.java)
//                .findUser(userId)
//
//        return
//    }

    private suspend fun findFromNet(userId: String): Result<UserEntity> {
        return executeRequest(dataBlock = {
            val response = mRepository.obtainRetrofitService(UserService::class.java)
                    .userFind(userId)
            if (response.isSuccess()) {
                val user = response.result.buildUser()
                mDatabase.userDao().insert(user)
//                mDbHelper.save(User::class.java, user)
                emit(Result.Success(user))
            } else {
                emit(Result.Error(Exception("${response.message}")))
            }
        }, errorBlock = {
            emit(Result.Error(Exception("有错误")))
        })
    }


//    /**
//     * 搜索一个用户，优先本地缓存，
//     * 没有用然后再从网络拉取
//     */
//    suspend fun search(id: String): User? {
//        return findUserFromLocal(id) ?: return findFromNet(id)
//    }
//
//    /**
//     * 搜索一个用户，优先网络查询
//     * 没有用然后再从本地缓存拉取
//     */
//    suspend fun searchFirstOfNet(id: String): User? {
//        return findFromNet(id) ?: return findUserFromLocal(id)
//    }
}

private fun UserCard.toUserDomainModel(): UserInfoDomainModel = UserInfoDomainModel(this.name ?: "",
        this.portrait,
        this.desc,
        this.sex)