package com.mny.wan.im.data.db.entity

import androidx.room.*
import com.google.gson.annotations.SerializedName

import java.util.*

@Entity(tableName = "session",
        foreignKeys = [
            ForeignKey(entity = MessageEntity::class,
                    parentColumns = ["serverId"],
                    childColumns = ["messageId"])
        ],
        indices = [Index("serverId", unique = true),
            Index("messageId")])
data class SessionEntity(
        // 图片，接收者用户的头像，或者群的图片
        @ColumnInfo(name = "serverId")
        @SerializedName(value = "id")
        var serverId: String = "",
        // 图片，接收者用户的头像，或者群的图片
        @ColumnInfo
        var picture: String? = null,
        // 标题，用户的名称，或者群的名称
        @ColumnInfo
        var title: String? = null,

        // 显示在界面上的简单内容，是Message的一个描述
        @ColumnInfo
        var content: String? = null,

        // 类型，对应人，或者群消息
        @ColumnInfo
        var receiverType: Int = Message.RECEIVER_TYPE_NONE,

        // 未读数量，当没有在当前界面时，应当增加未读数量
        @ColumnInfo
        var unReadCount: Int = 0,

        // 最后更改时间
        @ColumnInfo
        var modifyAt: Date? = null,
        // 对应的消息，外键为Message的Id
        @ColumnInfo(name = "messageId")
        var messageId: String? = null
) {
    // Id, 是Message中的接收者User的Id或者群的Id
    @PrimaryKey(autoGenerate = true)
    var dbId: Long = 0
}