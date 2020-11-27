package com.mny.wan.pkg.domain.model

/**
 * Desc:
 */
data class UserInfoDomainModel(
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