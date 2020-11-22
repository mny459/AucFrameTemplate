package com.mny.wan.im.data.api.model.account

import com.mny.wan.im.data.db.entity.UserEntity
import com.mny.wan.im.domain.model.UserInfoDomainModel

/**
 * Desc:
 */
data class AccountRspModel(    // 用户基本信息
        val user: UserEntity,
        val account: String,// 当前登录的账号
        val token: String, // 当前登录成功后获取的Token,可以通过Token获取用户的所有信息
        val isBind: Boolean // 标示是否已经绑定到了设备PushId
)

fun AccountRspModel.toUserDomainModel(): UserInfoDomainModel = UserInfoDomainModel(user.name,
        user.portrait,
        user.desc,
        user.sex)


data class LoginModel(
        var account: String,
        val password: String,
        val pushId: String? = null)

data class RegisterModel(
        var account: String,
        val password: String,
        val name: String,
        val pushId: String? = null)
