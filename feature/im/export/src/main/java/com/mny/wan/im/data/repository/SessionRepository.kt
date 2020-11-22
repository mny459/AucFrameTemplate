package com.mny.wan.im.data.repository

import androidx.lifecycle.LiveData
import com.mny.wan.im.data.db.entity.Message
import com.mny.wan.im.data.db.entity.Session

interface SessionRepository {
    // 从本地查询Session
    fun findFromLocal(id: String): LiveData<Session>
    fun refreshNewestMsg(vararg message: Message)
    fun allSessions(): LiveData<List<Session>>
}