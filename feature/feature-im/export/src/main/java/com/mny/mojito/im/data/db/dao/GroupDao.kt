package com.mny.mojito.im.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.mny.mojito.im.data.db.entity.Group
import com.mny.mojito.im.data.db.entity.GroupEntity
import com.mny.mojito.im.domain.model.GroupMemberModel
import kotlinx.coroutines.flow.Flow

@Dao
interface GroupDao {
    @Query("select * from `group` where serverId = :serverId AND ownerUserId IN (SELECT DISTINCT(serverId) FROM user) limit 1")
    @Transaction
    fun findByServerId(serverId: String): LiveData<Group>

    /**
     * 查找本地所有的群
     * 附带查出群主和群成员
     */
    @Query("select * from `group` where ownerUserId IN (SELECT DISTINCT serverId FROM user) ORDER BY name")
    @Transaction
    fun queryAllGroups(): LiveData<List<Group>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertGroups(vararg groups: GroupEntity)

//    @Query("select * from groupMember where groupId =:groupId")
//    fun findGroupMembersByGroupId(groupId: String): Flow<List<GroupMemberModel>>

    @Query("select * from `group` where serverId = :serverId AND ownerUserId IN (SELECT DISTINCT(serverId) FROM user) limit 1")
    @Transaction
    fun findGroupByServerId(serverId: String): Group
}