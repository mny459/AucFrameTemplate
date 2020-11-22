package com.mny.wan.im.data.card

import com.mny.wan.im.data.db.entity.GroupMemberEntity
import java.util.*

data class GroupMemberCard(var id: String = "",
                           val alias: String?,
                           val isAdmin: Boolean = false,
                           val isOwner: Boolean = false,
                           val userId: String,
                           val groupId: String,
                           val modifyAt: Date? = null) {
    fun build(): GroupMemberEntity {
        return GroupMemberEntity(serverId = id,
                alias = alias ?: "",
                isAdmin = isAdmin,
                isOwner = isOwner,
                modifyAt = modifyAt,
                userId = userId,
                groupId = groupId)
    }
}