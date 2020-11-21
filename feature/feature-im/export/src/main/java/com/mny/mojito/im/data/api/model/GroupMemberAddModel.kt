package com.mny.mojito.im.data.api.model

import java.util.*

data class GroupMemberAddModel(val users: Set<String> = HashSet())