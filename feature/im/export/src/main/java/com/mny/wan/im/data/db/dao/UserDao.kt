package com.mny.wan.im.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.mny.wan.im.data.db.entity.UserEntity

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg user: UserEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(user: UserEntity): Int

    @Query("SELECT * FROM user WHERE serverId = :userId")
    fun queryUserById(userId: String): LiveData<UserEntity>



    @Query("SELECT * FROM user WHERE isFollow = :isFollow ORDER BY modifyAt")
    fun queryContacts(isFollow: Boolean = true): LiveData<List<UserEntity>>

    @Query("SELECT * FROM user WHERE serverId = :userId")
    fun findUserByIdSync(userId: String): UserEntity
}