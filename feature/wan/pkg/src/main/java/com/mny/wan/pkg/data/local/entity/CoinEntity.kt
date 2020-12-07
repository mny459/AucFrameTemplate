package com.mny.wan.pkg.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.mny.wan.pkg.data.remote.model.BeanCoin

/**
 * 积分
 */
@Entity(
    tableName = "coin",
    foreignKeys = [ForeignKey(
        entity = UserEntity::class,
        parentColumns = ["id"],
        childColumns = ["userId"]
    )],
    indices = [Index("userId", unique = true)]
)
data class CoinEntity(

    val userId: Int = 0,
    val coinCount: Int = 0, // 积分
    val rank: Int = 0, // 排名
    val level: Int = 0, // 18
    val username: String = "",
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}
