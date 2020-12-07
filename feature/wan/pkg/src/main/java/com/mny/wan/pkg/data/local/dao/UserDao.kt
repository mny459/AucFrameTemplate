package com.mny.wan.pkg.data.local.dao

import androidx.room.*
import com.mny.wan.pkg.data.local.entity.CoinEntity
import com.mny.wan.pkg.data.local.entity.CollectionEntity
import com.mny.wan.pkg.data.local.entity.UserEntity
import com.mny.wan.pkg.data.local.view.User
import kotlinx.coroutines.flow.Flow

/**
 *
 */
@Dao
interface UserDao {
    /**
     * 插入一个用户
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(userEntity: UserEntity, collections: List<CollectionEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCoin(coinEntity: CoinEntity)

    /**
     * 插入用户的收藏列表
     */
    @Transaction
    @Query("select * from user where id = :id AND id IN (SELECT DISTINCT(userId) FROM coin) AND id IN (SELECT DISTINCT(userId) FROM collection) limit 1")
    fun queryUserById(id: Int): Flow<User>
}