package com.mny.login.data.remote.model

import android.text.TextUtils
import com.mny.wan.pkg.domain.model.UserInfoDomainModel

/**
 * Desc:
 */
data class UserInfoModel(
        val admin: Boolean,
        val chapterTops: List<Any>,
        val collectIds: List<Int>,
        val email: String,
        val icon: String,
        val id: Int,
        val nickname: String,
        val password: String,
        val publicName: String,
        val token: String,
        val type: Int,
        val username: String
)

fun UserInfoModel.toDomainModel(): UserInfoDomainModel {
    val name = when {
        !TextUtils.isEmpty(username) -> username
        !TextUtils.isEmpty(publicName) -> publicName
        else -> publicName
    }
    return UserInfoDomainModel(name, collectIds)
}
