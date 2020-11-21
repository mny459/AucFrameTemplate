package com.mny.mojito.im.data.db.entity

import androidx.room.*
import com.google.gson.annotations.SerializedName
import java.util.*

@Entity(
        tableName = "message",
        foreignKeys = [
            ForeignKey(entity = UserEntity::class, parentColumns = ["serverId"], childColumns = ["senderId"]),
            ForeignKey(entity = GroupEntity::class, parentColumns = ["serverId"], childColumns = ["groupId"]),
            ForeignKey(entity = UserEntity::class, parentColumns = ["serverId"], childColumns = ["receiverId"])
        ],
        indices = [
            Index("serverId", unique = true),
            Index("senderId"),
            Index("groupId"),
            Index("receiverId")
        ]
)
data class MessageEntity(
        @ColumnInfo(name = "serverId")// 主键
        @SerializedName(value = "id")
        var serverId: String,
        // 内容
        @ColumnInfo
        var content: String? = null,
        // 附属信息
        @ColumnInfo
        var attach: String? = null,
        @ColumnInfo
        var type: Int = 0,        // 消息类型
        @ColumnInfo
        var createAt: Date? = null,// 创建时间
        @ColumnInfo
        var status: Int = 0,// 当前消息的状态
        // 发送者 外键
        // 在加载Message信息的时候，User并没有，懒加载
        var senderId: String = "",
        // 接收者群外键
        var groupId: String? = "",
        // 接收者人外键
        var receiverId: String? = ""
) {
    //主键
    @PrimaryKey(autoGenerate = true)
    var dbId: Long = 0
}