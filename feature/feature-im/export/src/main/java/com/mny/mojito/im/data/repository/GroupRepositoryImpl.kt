package com.mny.mojito.im.data.repository

import androidx.annotation.Nullable
import androidx.lifecycle.LiveData
import com.mny.mojito.http.Result
import com.mny.mojito.im.base.BaseRepository
import com.mny.mojito.im.data.api.model.GroupCreateModel
import com.mny.mojito.im.data.api.service.UserService
import com.mny.mojito.im.data.card.GroupCard
import com.mny.mojito.im.data.card.GroupMemberCard
import com.mny.mojito.im.data.db.dao.GroupDao
import com.mny.mojito.im.data.db.dao.GroupMemberDao
import com.mny.mojito.im.data.db.dao.UserDao
import com.mny.mojito.im.data.db.entity.Group
import com.mny.mojito.im.data.db.entity.GroupEntity
import com.mny.mojito.im.data.db.entity.GroupMember
import com.mny.mojito.im.data.db.entity.GroupMemberEntity
import com.mny.mojito.im.domain.model.GroupMemberModel
import com.mny.mojito.im.domain.repository.GroupRepository
import com.mny.mojito.im.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GroupRepositoryImpl @Inject constructor(private val mGroupDao: GroupDao,
                                              private val mGroupMemberDao: GroupMemberDao) : BaseRepository(), GroupRepository {

    @Nullable
    override suspend fun findFromLocal(groupId: String): LiveData<Group> {
        return mGroupDao.findByServerId(groupId)
    }

    override fun findGroupFromLocal(groupId: String): Group {
        return mGroupDao.findGroupByServerId(groupId)
    }

//    override fun findLocalGroupMembers(groupId: String): Flow<List<GroupMemberModel>> {
//        return mGroupDao.findGroupMembersByGroupId(groupId)
//    }

    override suspend fun findFromNet(groupId: String): Result<GroupCard> {
        return executeRequest(dataBlock = {
            val response = mRepository.obtainRetrofitService(UserService::class.java)
                    .groupFind(groupId)
            if (response.isSuccess()) {
                emit(Result.Success(response.result))
            } else {
                emit(Result.Error(Exception("${response.message}")))
            }
        }, errorBlock = {
            emit(Result.Error(Exception("有错误")))
        })
    }


    override fun loadAllGroups(): LiveData<List<Group>> {
        return mGroupDao.queryAllGroups()
    }

    override suspend fun createGroup(model: GroupCreateModel): Result<GroupCard> {
        return executeRequest(dataBlock = {
            val response = mRepository.obtainRetrofitService(UserService::class.java)
                    .groupCreate(model)
            if (response.isSuccess()) {
                emit(Result.Success(response.result))
            } else {
                emit(Result.Error(Exception("${response.message}")))
            }
        }, errorBlock = {
            emit(Result.Error(Exception("有错误")))
        })
    }

    override suspend fun searchGroup(name: String): Result<List<GroupCard>> {
        return executeRequest(dataBlock = {
            val response = mRepository.obtainRetrofitService(UserService::class.java)
                    .groupSearch(name)
            if (response.isSuccess()) {
                emit(Result.Success(response.result))
            } else {
                emit(Result.Error(Exception("${response.message}")))
            }
        }, errorBlock = {
            emit(Result.Error(Exception("有错误")))
        })
    }

    override suspend fun myGroups(): Result<List<GroupCard>> {
        return executeRequest(dataBlock = {
            val response = mRepository.obtainRetrofitService(UserService::class.java)
                    .groups("")
            if (response.isSuccess()) {
                emit(Result.Success(response.result))
            } else {
                emit(Result.Error(Exception("${response.message}")))
            }
        }, errorBlock = {
            emit(Result.Error(Exception("有错误")))
        })
    }

    override suspend fun getGroupMembers(groupId: String): Result<List<GroupMemberCard>> {
        return executeRequest(dataBlock = {
            val response = mRepository.obtainRetrofitService(UserService::class.java)
                    .groupMembers(groupId)
            if (response.isSuccess()) {
                emit(Result.Success(response.result))
            } else {
                emit(Result.Error(Exception("${response.message}")))
            }
        }, errorBlock = {
            emit(Result.Error(Exception("有错误")))
        })
    }

    override suspend fun saveGroups(vararg groups: GroupEntity) {
        mGroupDao.insertGroups(*groups)
    }

    override fun queryGroupAllMembers(groupId: String): LiveData<List<GroupMember>> {
        return mGroupMemberDao.queryGroupAllMembers(groupId)
    }

    override suspend fun saveGroupMembers(vararg members: GroupMemberEntity) {
        mGroupMemberDao.insertGroupMembers(*members)
    }
}