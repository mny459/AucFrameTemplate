package com.mny.mojito.data.room

import androidx.room.RoomDatabase

abstract class MojitoRoomDatabase : RoomDatabase() {
    companion object {
        @Volatile
        private var INSTANCE: MojitoRoomDatabase? = null



    }
}