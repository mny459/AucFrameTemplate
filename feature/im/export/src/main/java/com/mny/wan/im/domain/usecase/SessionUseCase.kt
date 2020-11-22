package com.mny.wan.im.domain.usecase

import android.text.TextUtils
import androidx.lifecycle.LiveData
import com.blankj.utilcode.util.LogUtils
import com.mny.wan.im.data.db.entity.Message
import com.mny.wan.im.data.db.entity.Session
import com.mny.wan.im.data.repository.SessionRepository
import com.mny.wan.im.domain.repository.GroupRepository
import com.mny.wan.im.domain.repository.MessageRepository
import com.mny.wan.im.domain.repository.UserRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

/**
 * 会话辅助工具类
 */
class SessionUseCase constructor(
        private val mSessionRepository: SessionRepository,
        private val mMessageRepository: MessageRepository,
        private val mUserRepository: UserRepository,
        private val mGroupRepository: GroupRepository
)  {


    fun allSessions(): LiveData<List<Session>> {
      return  mSessionRepository.allSessions()
    }

    fun refreshToNow(session: Session) {
        GlobalScope.launch {
            var lMessage: Message? = null
            if (session.session.receiverType == Message.RECEIVER_TYPE_GROUP) {
                // 刷新当前对应的群的相关信息
                session.session.messageId?.apply {
                    lMessage = mMessageRepository.findLastWithGroup(this)
                }
                if (lMessage == null) {
                    // 如果没有基本信息
                    if (TextUtils.isEmpty(session.session.picture)
                            || TextUtils.isEmpty(session.session.title)) {
                        // 查询群
                        val group = mGroupRepository.findFromLocal(session.session.serverId)
                        group?.apply {
                            session.session.picture = group.value?.group?.picture
                            session.session.title = group.value?.group?.name
                        }
                    }
                    session.session.messageId = ""
                    session.message = null
                    session.session.content = ""
                    session.session.modifyAt = Date(System.currentTimeMillis())
                } else {
                    // 本地有最后一条聊天记录
                    if (TextUtils.isEmpty(session.session.picture)
                            || TextUtils.isEmpty(session.session.title)) {
                        // 如果没有基本信息, 直接从Message中去load群信息
                        val group = lMessage?.group
                        group?.apply {
                            session.session.picture = group?.picture
                            session.session.title = group?.name
                        }
                    }
                    session.session.messageId = lMessage?.message?.serverId
                    session.session.content = lMessage?.sampleContent
                    session.session.modifyAt = lMessage?.message?.createAt
                }
            } else {
                // 和人聊天的
                lMessage = mMessageRepository.findLastWithUser(session.session.serverId)
                if (lMessage == null) {
                    // 我和他的消息已经删除完成了
                    // 如果没有基本信息
                    if (TextUtils.isEmpty(session.session.picture)
                            || TextUtils.isEmpty(session.session.title)) {
                        // 查询人
                        val user = mUserRepository.findUserFromLocal(session.session.serverId).value
                        user?.let {
                            session.session.picture = it.portrait
                            session.session.title = it.name
                        }
                    }
                    session.message = null
                    session.session.content = ""
                    session.session.modifyAt = Date(System.currentTimeMillis())
                } else {
                    // 我和他有消息来往

                    // 如果没有基本信息
                    if (TextUtils.isEmpty(session.session.picture)
                            || TextUtils.isEmpty(session.session.title)) {
                        // 查询人
                        val other = lMessage?.other
                        session.session.picture = other?.portrait
                        session.session.title = other?.name
                    }
                    session.message = lMessage!!.message
                    session.session.content = lMessage?.sampleContent
                    session.session.modifyAt = lMessage?.message?.createAt
                }
            }
            LogUtils.d("refreshToNow $session")
        }

    }
}