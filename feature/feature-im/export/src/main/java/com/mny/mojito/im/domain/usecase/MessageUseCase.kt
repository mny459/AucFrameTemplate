package com.mny.mojito.im.domain.usecase

import android.text.TextUtils
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.asFlow
import com.blankj.utilcode.util.LogUtils
import com.mny.mojito.http.Result
import com.mny.mojito.im.data.api.message.MsgCreateModel
import com.mny.mojito.im.data.card.MessageCard
import com.mny.mojito.im.data.db.entity.*
import com.mny.mojito.im.data.repository.SessionRepository
import com.mny.mojito.im.domain.repository.GroupRepository
import com.mny.mojito.im.domain.repository.MessageRepository
import com.mny.mojito.im.domain.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import javax.inject.Inject

/**
 * 消息的核心思路
 */
class MessageUseCase @Inject constructor(
        private val mMessageRepository: MessageRepository,
        private val mUserRepository: UserRepository,
        private val mGroupRepository: GroupRepository,
        private val mSessionRepository: SessionRepository
) {

    fun getMessagesByReceiver(serverId: String, isGroup: Boolean): LiveData<List<Message>> {
        return mMessageRepository.getMessagesByReceiver(serverId, isGroup)
    }

    @WorkerThread
    suspend fun push(model: MsgCreateModel): Flow<Int> {
        return flow {
            LogUtils.d("push ${Thread.currentThread().name}")
            // 成功状态：如果是一个已经发送过的消息，则不能重新发送
            // 正在发送状态：如果是一个消息正在发送，则不能重新发送
            val message = mMessageRepository.findFromLocal(model.id).value
            if (message != null && message.status != Message.STATUS_FAILED) {
                emit(0)
                return@flow
            }
            // TODO 如果是文件类型的（语音，图片，文件），需要先上传后才发送

            // 我们在发送的时候需要通知界面更新状态，Card;
            val card: MessageCard = model.buildCard()
            // 1. 构建消息
            emit(card.status)
            handleMessage(card)
            when (val result = mMessageRepository.pushMsg(model)) {
                is Result.Success -> {
                    LogUtils.d("消息发送成功")
                    result.data?.apply {
                        emit(card.status)
                        card.status = Message.STATUS_DONE
                        handleMessage(card)
                    }
                }
                is Result.Error -> {
                    card.status = Message.STATUS_FAILED
                    emit(card.status)
                    handleMessage(card)
                }
                else -> {
                }
            }
        }.flowOn(Dispatchers.IO)
    }


    /**
     * 消息的统一处理,包括发送和接收
     */
    @WorkerThread
    suspend fun handleMessage(vararg cards: MessageCard) {
        val messageEntities = mutableListOf<MessageEntity>()
        val messages = mutableListOf<Message>()
        for (card in cards) {
            // 卡片基础信息过滤，错误卡片直接过滤
            if (TextUtils.isEmpty(card.senderId)
                    || TextUtils.isEmpty(card.id)
                    || (TextUtils.isEmpty(card.receiverId)
                            && TextUtils.isEmpty(card.groupId))) continue

            // 消息卡片有可能是推送过来的，也有可能是直接本地直接造的
            // 推送来的代表服务器一定有，我们可以查询到（本地有可能有，有可能没有）
            // 如果是直接造的，那么先存储本地，后发送网络

            // 发送消息流程：写消息->存储本地->发送网络->网络返回->刷新本地状态
            var messageEntity = mMessageRepository.findFromLocal(card.id).value

            if (messageEntity != null) {
                /// 1. 本地已经存在这条消息了
                // 消息本身字段从发送后就不变化了，如果收到了消息，
                // 本地有，同时本地显示消息状态为完成状态，则不必处理，
                // 因为此时回来的消息和本地一定一摸一样

                // 如果本地消息显示已经完成则不做处理
                if (messageEntity.status == Message.STATUS_DONE) continue

                // 新状态为完成才更新服务器时间，不然不做更新
                if (card.status == Message.STATUS_DONE) {
                    // 代表网络发送成功，此时需要修改时间为服务器的时间
                    messageEntity.createAt = card.createAt

                    // 如果没有进入判断，则代表这个消息是发送失败了，
                    // 重新进行数据库更新而而已
                }

                // 更新一些会变化的内容
                messageEntity.content = card.content
                messageEntity.attach = card.attach
                // 更新状态
                messageEntity.status = card.status
            } else {
                // 2. 没找到本地消息，初次在数据库存储
                // 2.1 构建发送者、接受者（个人或群）
                var sender: UserEntity? = mUserRepository.findUserById(card.senderId!!)
                var receiver: UserEntity? = null
                var group: Group? = null
                if (!TextUtils.isEmpty(card.receiverId)) {
                    receiver = mUserRepository.findUserById(card.receiverId!!)
                } else if (!TextUtils.isEmpty(card.groupId)) {
                    group = mGroupRepository.findGroupFromLocal(card.groupId!!)
                }

                // 接收者总有一个
                if (sender == null || (receiver == null && group == null))
                    continue
                val message = card.build(sender, receiver, group?.group)
                mMessageRepository.saveMessages(message.message)
                mSessionRepository.refreshNewestMsg(message)
                messageEntity = message.message
            }
            messageEntities.add(messageEntity)
        }
        if (messageEntities.isNotEmpty()) {
            mMessageRepository.saveMessages(*messageEntities.toTypedArray())
        }
    }

}