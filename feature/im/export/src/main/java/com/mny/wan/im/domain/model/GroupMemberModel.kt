package com.mny.wan.im.domain.model

import androidx.room.DatabaseView

@DatabaseView("SELECT groupMember.alias AS alias, groupMember.userId AS userId, user.name AS name, user.portrait AS portrait FROM groupMember INNER JOIN user ON user.serverId = groupMember.userId")
data class GroupMemberModel(var userId: String, val name: String, val alias: String?, val portrait: String)