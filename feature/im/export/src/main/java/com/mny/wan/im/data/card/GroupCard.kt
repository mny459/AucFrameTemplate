package com.mny.wan.im.data.card

import com.mny.wan.im.data.db.entity.Group
import com.mny.wan.im.data.db.entity.GroupEntity
import com.mny.wan.im.data.db.entity.UserEntity
import java.util.*

data class GroupCard(
        var id: String,
        var name: String,
        var desc: String,
        var picture: String,
        var ownerId: String,
        var notifyLevel: Int = 0,
        var joinAt: Date?,
        var modifyAt: Date?
) {

    /**
     * 把一个群的信息，build为一个群Model
     * 由于卡片中有创建者的Id，但是没有创建者这个人的Model；
     * 所以Model需求在外部准备好传递进来
     *
     * @param owner 创建者
     * @return 群信息
     */
    fun buildGroup(owner: UserEntity): Group {
        val groupEntity = GroupEntity(
                serverId = id,
                name = name,
                desc = desc,
                picture = picture,
                notifyLevel = notifyLevel,
                joinAt = joinAt,
                modifyAt = modifyAt,
                ownerUserId = ownerId
        )
        return Group(groupEntity, owner)
    }

    fun buildEntity(): GroupEntity {
        return GroupEntity(
                serverId = id,
                name = name,
                desc = desc,
                picture = picture,
                notifyLevel = notifyLevel,
                joinAt = joinAt,
                modifyAt = modifyAt,
                ownerUserId = ownerId
        )
    }
}