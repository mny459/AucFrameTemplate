package com.mny.mojito.im.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.mny.mojito.im.data.db.entity.Session
import com.mny.mojito.im.data.db.entity.SessionEntity

@Dao
interface SessionDao {
    @Query("SELECT * FROM session WHERE messageId IN (SELECT DISTINCT(serverId) FROM message) ORDER BY modifyAt")
    @Transaction
    fun allSessions(): LiveData<List<Session>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(session: SessionEntity)

    @Query("SELECT * FROM session WHERE serverId = :id  ORDER BY modifyAt limit 1")
    @Transaction
    fun findFromLocal(id: String): LiveData<Session>
}