package com.mny.mojito.data.room

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

object RoomHelper {
    fun <T : RoomDatabase> getDatabase(context: Context, kClass: Class<T>, name: String, roomCallback: List<RoomDatabase.Callback>): T {
        return synchronized(this) {
            val builder = Room.databaseBuilder(context.applicationContext, kClass, name)
                    .fallbackToDestructiveMigration()
            roomCallback.forEach { builder.addCallback(it) }
            builder.build()
        }
    }
}