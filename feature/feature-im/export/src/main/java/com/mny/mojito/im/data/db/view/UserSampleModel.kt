package com.mny.mojito.im.data.db.view


/**
 * 用户基础信息的Model，可以和数据库进行查询
 */
data class UserSampleModel(
        var id: String? = null,
        var name: String? = null,
        var portrait: String? = null
)
