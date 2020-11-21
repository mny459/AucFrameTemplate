package com.mny.mojito.data.room

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.CoroutineScope
import kotlin.reflect.KClass

abstract class MojitoRoomDatabase : RoomDatabase() {
    companion object {
        @Volatile
        private var INSTANCE: MojitoRoomDatabase? = null



    }
}