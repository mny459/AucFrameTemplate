package com.mny.wan.pkg.data.local.view

import androidx.annotation.Nullable
import androidx.room.Embedded
import androidx.room.Relation
import com.mny.wan.pkg.data.local.entity.CoinEntity
import com.mny.wan.pkg.data.local.entity.CollectionEntity
import com.mny.wan.pkg.data.local.entity.UserEntity

/**
 * 用户信息
 */
data class User(
    @Embedded
    val info: UserEntity, // 个人信息
    @Relation(parentColumn = "id", entityColumn = "userId")
    val collection: List<CollectionEntity> = emptyList(), // 收藏id
    @Nullable
    @Relation(parentColumn = "id", entityColumn = "userId")
    var coin: CoinEntity?, // 排名信息
)