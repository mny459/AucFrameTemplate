package com.mny.mojito.im.data.db.entity

import androidx.room.*
import com.google.gson.annotations.SerializedName
import java.util.*

@Entity(tableName = "group",
        foreignKeys = [
            ForeignKey(entity = UserEntity::class, parentColumns = ["serverId"], childColumns = ["ownerUserId"])
        ],
        indices = [Index("serverId", unique = true),
            Index("ownerUserId")])
data class GroupEntity(
        @ColumnInfo(name = "serverId")// 主键
        @SerializedName(value = "id")
        var serverId: String,
        // 群名称
        var name: String? = null,
        // 群描述
        var desc: String? = null,
        // 群图片
        var picture: String? = null,
        // 我在群中的消息通知级别-对象是我当前登录的账户
        var notifyLevel: Int = 0,
        // 我的加入时间
        var joinAt: Date? = null,
        // 信息修改时间
        var modifyAt: Date? = null,
        var ownerUserId: String? = null// 群主ID
) {
    // 群Id
    @PrimaryKey(autoGenerate = true)
    var dbId: Long = 0
}