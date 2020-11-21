package com.mny.mojito.im.data.api.model.user

/**
 * Desc:
 */
data class UserUpdateModel(
        val name: String,
        val portrait: String,
        val desc: String,
        val sex: Int
)