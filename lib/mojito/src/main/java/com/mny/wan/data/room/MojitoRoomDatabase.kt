package com.mny.wan.data.room

import androidx.room.RoomDatabase

abstract class MojitoRoomDatabase : RoomDatabase() {
    companion object {
        @Volatile
        private var INSTANCE: MojitoRoomDatabase? = null



    }
}