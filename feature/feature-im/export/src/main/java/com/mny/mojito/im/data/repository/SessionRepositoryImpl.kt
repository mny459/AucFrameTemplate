package com.mny.mojito.im.data.repository

import android.text.TextUtils
import androidx.lifecycle.LiveData
import com.blankj.utilcode.util.LogUtils
import com.mny.mojito.im.data.db.dao.GroupDao
import com.mny.mojito.im.data.db.dao.MessageDao
import com.mny.mojito.im.data.db.dao.SessionDao
import com.mny.mojito.im.data.db.dao.UserDao
import com.mny.mojito.im.data.db.entity.Message
import com.mny.mojito.im.data.db.entity.Session
import com.mny.mojito.im.data.db.entity.UserEntity
import java.util.*
import javax.inject.Inject

class SessionRepositoryImpl @Inject constructor(
        private val mSessionDao: SessionDao,
        private val mMessageDao: MessageDao,
        private val mGroupDao: GroupDao,
        private val mUserDao: UserDao
) : SessionRepository {

    override fun allSessions(): LiveData<List<Session>> = mSessionDao.allSessions()

    // 从本地查询Session
    override fun findFromLocal(id: String): LiveData<Session> {
        return mSessionDao.findFromLocal(id)
    }

    override fun refreshNewestMsg(vararg messages: Message) {
        // 标示一个Session的唯一性
        val identifies: MutableSet<Session.Identify> = mutableSetOf()
        for (message in messages) {
            val identify = Session.createSessionIdentify(message)
            identifies.add(identify)
        }
        for (identify in identifies) {
            var session = mSessionDao.findFromLocal(identify.id).value
            if (session == null) {
                // 第一次聊天，创建一个你和对方的一个会话
                session = Session.createFromIdentify(identify);
            }
            // 把会话，刷新到当前Message的最新状态
            if (session.session.receiverType == Message.RECEIVER_TYPE_GROUP) {
                identify.message?.apply {
                    session.session.picture = group?.picture
                    session.session.title = group?.name
                    session.session.messageId = message.serverId
                    session.message = message
                    session.session.content = sampleContent
                    session.session.modifyAt = message.createAt
                }
            } else {
                identify.message?.apply {
                    session.session.picture = receiver?.portrait
                    session.session.title = receiver?.name
                    session.session.messageId = message.serverId
                    session.message = message
                    session.session.content = sampleContent
                    session.session.modifyAt = message.createAt
                }
            }
            LogUtils.d("refreshToNow $session")
            mSessionDao.insert(session = session.session)
        }

    }
}