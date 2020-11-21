package com.mny.mojito.im.data.db.view


/**
 * 群成员对应的用户的简单信息表
 */
data class MemberUserModel(
        var userId: String? = null,// User-id/Member-userId
        var name: String? = null,// User-name
        var alias: String? = null,// Member-alias
        var portrait: String? = null// User-portrait
)