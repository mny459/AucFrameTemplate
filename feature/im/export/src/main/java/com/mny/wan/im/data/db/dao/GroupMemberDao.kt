package com.mny.wan.im.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.mny.wan.im.data.db.entity.GroupMember
import com.mny.wan.im.data.db.entity.GroupMemberEntity

@Dao
interface GroupMemberDao {
    /**
     * 查找本地所有的群
     * 附带查出群主和群成员
     */
    @Query("select * from groupMember where groupId = :groupId AND groupId IN (SELECT DISTINCT(serverId) FROM `group`) AND userId IN (SELECT DISTINCT(serverId) FROM user) ORDER BY alias")
    @Transaction
    fun queryGroupAllMembers(groupId: String): LiveData<List<GroupMember>>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertGroupMembers(vararg members: GroupMemberEntity)
}