package com.mny.wan.pkg.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.blankj.utilcode.util.LogUtils
import com.mny.wan.pkg.data.local.dao.UserDao
import com.mny.wan.pkg.data.local.entity.CoinEntity
import com.mny.wan.pkg.data.local.entity.CollectionEntity
import com.mny.wan.pkg.data.local.entity.UserEntity


@Database(
    entities = [UserEntity::class, CollectionEntity::class, CoinEntity::class],
    version = 1,
    exportSchema = true
)
public abstract class WanDataBase : RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: WanDataBase? = null

        fun getDatabase(context: Context): WanDataBase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WanDataBase::class.java,
                    "mojito_wan"
                ).addCallback(
                    object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            LogUtils.d("onCreate")
                        }

                        override fun onOpen(db: SupportSQLiteDatabase) {
                            super.onOpen(db)
                            LogUtils.d("onOpen")
                        }
                    }
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}