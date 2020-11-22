package com.mny.wan.im.domain.model

import com.mny.wan.im.data.db.entity.UserEntity


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