package com.mny.wan.im.data.db.entity

import androidx.room.*
import com.google.gson.annotations.SerializedName
import java.util.*

@Entity(tableName = "groupMember",
        foreignKeys = [
            ForeignKey(entity = UserEntity::class, parentColumns = ["serverId"], childColumns = ["userId"]),
            ForeignKey(entity = GroupEntity::class, parentColumns = ["serverId"], childColumns = ["groupId"])
        ],
        indices = [Index("groupId"), Index("userId")])
data class GroupMemberEntity(
        @ColumnInfo(name = "serverId")// 主键
        @SerializedName(value = "id")
        var serverId: String,
        // 别名，备注名
        @ColumnInfo
        var alias: String? = null,

        // 是否是管理员
        @ColumnInfo
        var isAdmin: Boolean = false,

        // 是否是群创建者
        @ColumnInfo
        var isOwner: Boolean = false,
        // 更新时间
        @ColumnInfo
        var modifyAt: Date? = null,
        // 对应的用户外键
        var userId: String = "",
        // 对应的群外键
        var groupId: String = ""
) {
    @PrimaryKey(autoGenerate = true)
    var dbId: Long = 0 // 主键

    companion object {
        // 消息通知级别
        const val NOTIFY_LEVEL_INVALID = -1 // 关闭消息
        const val NOTIFY_LEVEL_NONE = 0 // 正常
    }
}