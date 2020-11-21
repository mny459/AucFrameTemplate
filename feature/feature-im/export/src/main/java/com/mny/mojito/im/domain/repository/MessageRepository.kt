package com.mny.mojito.im.domain.repository

import androidx.lifecycle.LiveData
import com.mny.mojito.http.Result
import com.mny.mojito.im.data.api.message.MsgCreateModel
import com.mny.mojito.im.data.card.MessageCard
import com.mny.mojito.im.data.db.entity.Message
import com.mny.mojito.im.data.db.entity.MessageEntity

interface MessageRepository {
    suspend fun pushMsg(model: MsgCreateModel): Result<MessageCard>
    fun findFromLocal(serverId: String): LiveData<MessageEntity>
    suspend fun findLastWithGroup(messageId: String): Message?
    suspend fun findLastWithUser(messageId: String): Message?
    fun getMessagesByReceiver(serverId: String, group: Boolean): LiveData<List<Message>>
    fun saveMessages(vararg message: MessageEntity)
}