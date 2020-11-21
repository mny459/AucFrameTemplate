package com.mny.mojito.im.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.mny.mojito.im.data.db.entity.Message
import com.mny.mojito.im.data.db.entity.MessageEntity

@Dao
interface MessageDao {
    @Query("SELECT * FROM message WHERE serverId = :messageId")
    fun queryMessageById(messageId: String): LiveData<MessageEntity>

    @Query("SELECT * FROM message WHERE serverId = :messageId order by createAt")
    fun queryLastMessageById(messageId: String): LiveData<MessageEntity>

    @Query("SELECT * FROM message WHERE serverId = :messageId order by createAt limit 1")
    @Transaction
    fun queryMessageWithGroup(messageId: String): LiveData<Message>

    @Query("SELECT * FROM message WHERE serverId = :messageId order by createAt limit 1")
    @Transaction
    fun queryMessageWithUser(messageId: String): LiveData<Message>

    // TODO 补充时间过滤
    @Query("SELECT * FROM message WHERE groupId = :groupServerId order by createAt limit 30")
    @Transaction
    fun queryGroupAllMessage(groupServerId: String): LiveData<List<Message>>

    @Query("SELECT * FROM message WHERE (receiverId = :userServerId AND receiverId IN (SELECT DISTINCT(serverId) FROM user)) OR (senderId =:userServerId and senderId IN (SELECT DISTINCT(serverId) FROM user) ) order by createAt limit 30")
    @Transaction
    fun queryUserAllMessage(userServerId: String): LiveData<List<Message>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMessage(vararg message: MessageEntity)
}