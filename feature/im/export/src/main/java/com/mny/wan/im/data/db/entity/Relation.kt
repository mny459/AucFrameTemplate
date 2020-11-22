package com.mny.wan.im.data.db.entity

import androidx.room.Embedded
import androidx.room.Ignore
import androidx.room.Relation
import com.mny.wan.im.data.factory.AccountManager
import com.mny.wan.im.utils.DiffUiDataCallback

/**
 * 群主
 */
data class Group(
        @Embedded
        val group: GroupEntity,
        @Relation(
                parentColumn = "ownerUserId",
                entityColumn = "serverId"
        )
        val owner: UserEntity,
        @Relation(
                parentColumn = "serverId",
                entityColumn = "groupId"
        )
        val members: List<GroupMemberEntity> = emptyList()
) {
    @Ignore
    var groupMemberCount: Long = -1
    /// TODO 显示群成员信息
    @Ignore
    var holder: String = ""
    //    // 获取当前群的成员数量，使用内存缓存
//    fun getGroupMemberCount(): Long {
//        if (groupMemberCount == -1L) {
//            // -1 没有初始化
//            groupMemberCount = GroupHelper.getMemberCount(id)
//        }
//        return groupMemberCount
//    }
//
//    private var groupLatelyMembers: List<MemberUserModel>? = null
//
//    // 获取当前群对应的成员的信息，只加载4个信息
//    fun getLatelyGroupMembers(): List<MemberUserModel>? {
//        if (groupLatelyMembers == null || groupLatelyMembers!!.isEmpty()) {
//            // 加载简单的用户信息，返回4条，至多
//            groupLatelyMembers = GroupHelper.getMemberUsers(id, 4)
//        }
//        return groupLatelyMembers
//    }
}

/**
 * 会话
 */
data class Session(
        @Embedded val session: SessionEntity,
        @Relation(
                parentColumn = "messageId",
                entityColumn = "serverId",
                entity = MessageEntity::class
        )
        var message: MessageEntity?
) {
    /**
     * 对于会话信息，最重要的部分进行提取
     * 其中我们主要关注两个点：
     * 一个会话最重要的是标示是和人聊天还是在群聊天；
     * 所以对于这点：Id存储的是人或者群的Id
     * 紧跟着Type：存储的是具体的类型（人、群）
     * equals 和 hashCode 也是对两个字段进行判断
     */
    class Identify(val message: Message? = null, val messageEntity: MessageEntity? = null) {
        var id: String = ""
        var type = 0
        override fun equals(o: Any?): Boolean {
            if (this === o) return true
            if (o == null || javaClass != o.javaClass) return false
            val identify = o as Identify
            return (type == identify.type
                    && if (id != null) id == identify.id else identify.id == null)
        }

        override fun hashCode(): Int {
            var result = if (id != null) id.hashCode() else 0
            result = 31 * result + type
            return result
        }
    }

    companion object {
        fun createSessionIdentify(message: Message): Identify {
            val identify = Identify(message, message.message)
            if (message.group == null) {
                identify.type = Message.RECEIVER_TYPE_NONE
                val other: UserEntity = message.other
                identify.id = other.serverId
            } else {
                identify.type = Message.RECEIVER_TYPE_GROUP
                identify.id = message.group!!.serverId
            }
            return identify
        }

        fun createFromIdentify(identify: Identify): Session {
            val sessionEntity = SessionEntity(serverId = identify.id, receiverType = identify.type)
            val session = Session(session = sessionEntity, message = identify.message?.message)
            return session
        }
    }


}

/**
 * 消息
 */
data class Message(
        @Embedded var message: MessageEntity,
        @Relation(
                parentColumn = "senderId",
                entityColumn = "serverId",
                entity = UserEntity::class
        )
        var sender: UserEntity,
        @Relation(
                parentColumn = "receiverId",
                entityColumn = "serverId",
                entity = UserEntity::class
        )
        var receiver: UserEntity?,
        @Relation(
                parentColumn = "groupId",
                entityColumn = "serverId",
                entity = GroupEntity::class
        )
        var group: GroupEntity?
) : DiffUiDataCallback.UiDataDiffer<Message> {

    val sampleContent: String?
        get() {
            return when (message.type) {
                TYPE_PIC -> "[图片]"
                TYPE_AUDIO -> "🎵"
                TYPE_FILE -> "📃"
                else -> message.content
            }
        }


    /**
     * 当消息类型为普通消息（发送给人的消息）
     * 该方法用于返回，和我聊天的人是谁
     *
     *
     * 和我聊天，要么对方是发送者，要么对方是接收者
     *
     * @return 和我聊天的人
     */
    val other: UserEntity
        get() = if (AccountManager.getUserId() == sender.serverId) {
            receiver!!
        } else {
            sender
        }

    fun isSelfMsg() = sender.serverId == AccountManager.getUserId()

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val message = o as Message
        return (this.message.type == message.message.type
                && this.message.status == message.message.status
                && (this.message.serverId == message.message.serverId)
                && (this.message.content == message.message.content)
                && (this.message.attach == message.message.attach)
                && (this.message.createAt == message.message.createAt)
                && (this.group == message.group)
                && (this.sender == message.sender)
                && (this.receiver == message.receiver))
    }

    override fun hashCode(): Int {
        return this.message.serverId.hashCode()
    }

    override fun isSame(oldT: Message): Boolean {
        // 两个类，是否指向的是同一个消息
        return (this.message.serverId == oldT.message.serverId)
    }

    override fun isUiContentSame(oldT: Message): Boolean {
        // 对于同一个消息当中的字段是否有不同
        // 这里，对于消息，本身消息不可进行修改；只能添加删除
        // 唯一会变化的就是本地（手机端）消息的状态会改变
        return oldT === this || this.message.status == oldT.message.status
    }

    companion object {
        // 接收者类型
        const val RECEIVER_TYPE_NONE = 1
        const val RECEIVER_TYPE_GROUP = 2

        // 消息类型
        const val TYPE_STR = 1
        const val TYPE_STR_RIGHT = 11
        const val TYPE_PIC = 2
        const val TYPE_PIC_RIGHT = 21
        const val TYPE_FILE = 3
        const val TYPE_FILE_RIGHT = 31
        const val TYPE_AUDIO = 4
        const val TYPE_AUDIO_RIGHT = 41

        // 消息状态
        const val STATUS_DONE = 0 // 正常状态
        const val STATUS_CREATED = 1 // 创建状态
        const val STATUS_FAILED = 2 // 发送失败状态
    }
}

/**
 * 群成员
 */
data class GroupMember(
        @Embedded var groupMember: GroupMemberEntity?,
        @Relation(
                parentColumn = "userId",
                entityColumn = "serverId",
                entity = UserEntity::class
        ) var user: UserEntity?,
        @Relation(
                parentColumn = "groupId",
                entityColumn = "serverId",
                entity = GroupEntity::class
        ) var group: GroupEntity?
)