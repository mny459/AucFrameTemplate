package com.mny.mojito.im.domain.usecase

import androidx.lifecycle.LiveData
import com.blankj.utilcode.util.LogUtils
import com.mny.mojito.http.Result
import com.mny.mojito.im.data.api.model.GroupCreateModel
import com.mny.mojito.im.data.card.GroupCard
import com.mny.mojito.im.data.db.entity.Group
import com.mny.mojito.im.data.db.entity.GroupMember
import com.mny.mojito.im.domain.repository.GroupRepository
import com.mny.mojito.im.domain.repository.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GroupUseCase @Inject constructor(
        private val mGroupRepository: GroupRepository,
        private val mUserRepository: UserRepository
) {
    /// 查找群，优先级是先本地，再服务端
    suspend fun find(groupId: String): Result<Group> {
        var group = mGroupRepository.findFromLocal(groupId).value
        if (group == null) {
            when (val groupCard = mGroupRepository.findFromNet(groupId)) {
                is Result.Success -> {
                    groupCard.data?.apply {
                        when (val owner = mUserRepository.searchUserById(ownerId)) {
                            is Result.Success -> {
                                group = buildGroup(owner = owner.data!!)
                            }
                            is Result.Error -> {
                                return Result.Error(owner.exception)
                            }
                            else -> {
                            }
                        }
                    }
                }
                is Result.Error -> {
                    return Result.Error(groupCard.exception)
                }
                else -> {
                }
            }
        }
        return Result.Success(group)
    }

    fun loadAllGroups(viewModelScope: CoroutineScope): LiveData<List<Group>> {
        val allGroups = mGroupRepository.loadAllGroups()
        viewModelScope.launch {
            refreshGroups()
        }
        return allGroups
    }

    private suspend fun refreshGroups() {
        withContext(Dispatchers.IO) {
            when (val myGroups = mGroupRepository.myGroups()) {
                is Result.Success -> {
                    myGroups.data?.forEach {
                        mGroupRepository.saveGroups(it.buildEntity())
                    }
                }
                is Result.Error -> {
                }
                else -> {
                }
            }
        }
    }

    fun loadAllGroupMembers(viewModelScope: CoroutineScope, group: Group): LiveData<List<GroupMember>> {
        val members = mGroupRepository.queryGroupAllMembers(group.group.serverId)
        viewModelScope.launch { refreshGroupMembers(group) }
        return members
    }

    fun loadAllGroupMembers(groupId: String): LiveData<List<GroupMember>> {
        return mGroupRepository.queryGroupAllMembers(groupId)
    }

    private suspend fun refreshGroupMembers(group: Group) {
        withContext(Dispatchers.IO) {
            when (val result = mGroupRepository.getGroupMembers(group.group.serverId)) {
                is Result.Success -> {
                    result.data?.forEach {
                        LogUtils.d("refreshGroupMembers ${it.build()}")
                        mGroupRepository.saveGroupMembers(it.build())
                        withContext(Dispatchers.IO) {
                            // 会自动存
                            mUserRepository.searchUserById(it.userId)
                        }
                    }
                }
                is Result.Error -> {
                }
                else -> {
                }
            }
        }
    }

    suspend fun createGroup(model: GroupCreateModel): Result<GroupCard> {
        return mGroupRepository.createGroup(model)
    }

    suspend fun searchGroup(query: String): Result<List<GroupCard>> {
        return mGroupRepository.searchGroup(query)
    }

//    fun findAllGroupMembers(groupId: String): Flow<List<GroupMemberModel>> {
//        return mGroupRepository.findLocalGroupMembers(groupId)
//    }
}