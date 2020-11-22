package com.mny.wan.im.data.repository

import androidx.lifecycle.LiveData
import com.mny.wan.http.Result
import com.mny.wan.im.base.BaseRepository
import com.mny.wan.im.data.api.message.MsgCreateModel
import com.mny.wan.im.data.api.service.UserService
import com.mny.wan.im.data.card.MessageCard
import com.mny.wan.im.data.db.dao.MessageDao
import com.mny.wan.im.data.db.entity.Message
import com.mny.wan.im.data.db.entity.MessageEntity
import com.mny.wan.im.domain.repository.MessageRepository
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject

/**
 * 推送消息给服务端
 */
class MessageRepositoryImpl @Inject constructor(private val mMessageDao: MessageDao) : BaseRepository(), MessageRepository {
    override suspend fun pushMsg(model: MsgCreateModel): Result<MessageCard> {
        return executeRequest(dataBlock = {
            val response = mRepository.obtainRetrofitService(UserService::class.java)
                    .msgPush(model)
            if (response.isSuccess()) {
                emit(Result.Success(response.result))
            } else {
                emit(Result.Error(Exception("${response.message}")))
            }
        }, errorBlock = {
            emit(Result.Error(Exception("有错误")))
        })
    }

    /**
     * 从本地找消息
     */
    override fun findFromLocal(serverId: String): LiveData<MessageEntity> = mMessageDao.queryMessageById(serverId)


    /**
     * 查询一个消息，这个消息是一个群中的最后一条消息
     *
     * @param groupId 群Id
     * @return 群中聊天的最后一条消息
     */
    override suspend fun findLastWithGroup(messageId: String): Message? = suspendCancellableCoroutine {
        mMessageDao.queryMessageWithGroup(messageId).value
//        return withContext(Dispatchers.IO) {
//            SQLite.select()
//                    .from(Message::class.java)
//                    .where(Message_Table.group_id.eq(groupId))
//                    .orderBy(Message_Table.createAt, false) // 倒序查询
//                    .querySingle()
//        }
    }

    /**
     * 查询一个消息，这个消息是和一个人的最后一条聊天消息
     *
     * @param userId UserId
     * @return 聊天的最后一条消息
     */
    override suspend fun findLastWithUser(messageId: String): Message? = suspendCancellableCoroutine {
        mMessageDao.queryMessageWithUser(messageId).value
//        return withContext(Dispatchers.IO) {
//            SQLite.select()
//                    .from(Message::class.java)
//                    .where(OperatorGroup.clause()
//                            .and(Message_Table.sender_id.eq(userId))
//                            .and(Message_Table.group_id.isNull))
//                    .or(Message_Table.receiver_id.eq(userId))
//                    .orderBy(Message_Table.createAt, false) // 倒序查询
//                    .querySingle()
//        }
    }

    override fun getMessagesByReceiver(serverId: String, group: Boolean): LiveData<List<Message>> {
        return if (group) mMessageDao.queryGroupAllMessage(serverId)
        else mMessageDao.queryUserAllMessage(serverId)
    }

    override fun saveMessages(vararg message: MessageEntity) {
        mMessageDao.insertMessage(*message)
    }

}