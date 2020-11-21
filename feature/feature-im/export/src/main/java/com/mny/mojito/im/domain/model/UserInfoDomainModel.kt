package com.mny.mojito.im.domain.model

import com.mny.mojito.im.data.db.entity.UserEntity


/**
 * Desc:
 */
data class UserInfoDomainModel(
        val username: String,
        val avatar:String?,
        val desc: String?,
        val gender: Int
){
    fun isMan() = gender == UserEntity.SEX_MAN
}