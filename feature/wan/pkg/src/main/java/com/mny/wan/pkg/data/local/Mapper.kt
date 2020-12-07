package com.mny.wan.pkg.data.local

import com.mny.wan.pkg.data.local.entity.CoinEntity
import com.mny.wan.pkg.data.local.entity.CollectionEntity
import com.mny.wan.pkg.data.local.entity.UserEntity
import com.mny.wan.pkg.data.remote.model.BeanCoin
import com.mny.wan.pkg.data.remote.model.BeanUserInfo

fun BeanUserInfo.toEntity(): UserEntity {
    return UserEntity(id, nickname, publicName, username, type);
}


fun BeanCoin.toEntity(): CoinEntity {
    return CoinEntity(userId, coinCount, rank, level, username)
}


fun BeanUserInfo.toCollectionEntityList(): List<CollectionEntity> {
    val list = mutableListOf<CollectionEntity>()
    collectIds.forEach {
        list.add(CollectionEntity(id, it))
    }
    return list
}