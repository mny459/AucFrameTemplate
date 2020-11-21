package com.mny.mojito.im.presentation.group.adapter

import com.mny.mojito.im.data.db.entity.UserEntity

class GroupMember(val user: UserEntity, var isSelected: Boolean = false)