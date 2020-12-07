package com.mny.wan.pkg.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.mny.wan.pkg.data.remote.model.BeanUserInfo

@Entity(
    tableName = "collection",
    foreignKeys = [ForeignKey(
        entity = UserEntity::class,
        parentColumns = ["id"],
        childColumns = ["userId"]
    )],
    indices = [Index("userId")]
)
data class CollectionEntity(
    val userId: Int,
    val articleId: Int,
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}

