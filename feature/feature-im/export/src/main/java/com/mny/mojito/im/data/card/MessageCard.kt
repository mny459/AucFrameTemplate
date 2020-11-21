package com.mny.mojito.im.data.card

import com.mny.mojito.im.data.db.entity.GroupEntity
import com.mny.mojito.im.data.db.entity.Message
import com.mny.mojito.im.data.db.entity.MessageEntity
import com.mny.mojito.im.data.db.entity.UserEntity
import java.util.*


/**
 * 消息的卡片，用于接收服务器返回信息
 *
 * @author qiujuer Email:qiujuer@live.cn
 * @version 1.0.0
 */
class MessageCard {
    var id: String = ""
    var content: String? = null
    var attach: String? = null
    var type = 0
    var createAt: Date? = null
    var groupId: String? = null
    var senderId: String? = null
    var receiverId: String? = null

    // 两个额外的本地字段
    // transient 不会被Gson序列化和反序列化
    @Transient
    var status: Int = Message.STATUS_DONE //当前消息状态

    @Transient
    private val uploaded = false // 上传是否完成（对应的是文件）

    /**
     * 要构建一个消息，必须准备好3个外键对应的Model
     *
     * @param sender   发送者
     * @param receiver 接收者
     * @param group    接收者-群
     * @return 一个消息
     */
    fun build(sender: UserEntity, receiver: UserEntity?, group: GroupEntity?): Message {
        val messageEntity = MessageEntity(
                serverId = id,
                content = content,
                attach = attach,
                type = type,
                createAt = createAt,
                status = status,
                senderId = sender.serverId,
                groupId = group?.serverId,
                receiverId = receiver?.serverId
        )
        return Message(messageEntity, sender, receiver, group)
    }
}