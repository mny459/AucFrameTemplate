package com.mny.wan.im.domain.repository

import androidx.lifecycle.LiveData
import com.mny.wan.http.Result
import com.mny.wan.im.data.api.model.GroupCreateModel
import com.mny.wan.im.data.card.GroupCard
import com.mny.wan.im.data.card.GroupMemberCard
import com.mny.wan.im.data.db.entity.Group
import com.mny.wan.im.data.db.entity.GroupEntity
import com.mny.wan.im.data.db.entity.GroupMember
import com.mny.wan.im.data.db.entity.GroupMemberEntity

/**
 *
 */
interface GroupRepository {
    suspend fun findFromLocal(groupId: String): LiveData<Group>
    suspend fun findFromNet(groupId: String): Result<GroupCard>
    fun loadAllGroups(): LiveData<List<Group>>
    suspend fun createGroup(model: GroupCreateModel): Result<GroupCard>
    suspend fun searchGroup(name: String): Result<List<GroupCard>>

    /**
     * 我的群组
     */
    suspend fun myGroups(): Result<List<GroupCard>>
    suspend fun getGroupMembers(groupId: String): Result<List<GroupMemberCard>>
    suspend fun saveGroups(vararg groups: GroupEntity)
    fun queryGroupAllMembers(groupId: String): LiveData<List<GroupMember>>
    suspend fun saveGroupMembers(vararg members: GroupMemberEntity) {}

    // ======
    fun findGroupFromLocal(groupId: String): Group
//    fun findLocalGroupMembers(groupId: String): Flow<List<GroupMemberModel>>
}