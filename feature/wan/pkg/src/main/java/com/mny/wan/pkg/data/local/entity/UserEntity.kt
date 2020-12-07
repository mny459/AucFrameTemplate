package com.mny.wan.pkg.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.mny.wan.pkg.data.remote.model.BeanUserInfo

@Entity(tableName = "user", indices = [Index("id",unique = true)])
data class UserEntity(
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val nickname: String,
    val publicName: String,
    val username: String,
    val type: Int,
)


