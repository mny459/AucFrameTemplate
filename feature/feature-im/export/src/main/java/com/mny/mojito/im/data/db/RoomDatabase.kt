package com.mny.mojito.im.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.blankj.utilcode.util.LogUtils
import com.mny.mojito.im.data.db.dao.*
import com.mny.mojito.im.data.db.entity.*
import com.mny.mojito.im.domain.model.GroupMemberModel

@Database(entities = [
    GroupEntity::class,
    GroupMemberEntity::class,
    MessageEntity::class,
    SessionEntity::class,
    UserEntity::class
],
        views = [GroupMemberModel::class],
        version = 2,
        exportSchema = true)
@TypeConverters(Converters::class)
public abstract class AppRoomDatabase : RoomDatabase() {

    abstract fun groupDao(): GroupDao
    abstract fun groupMemberDao(): GroupMemberDao
    abstract fun messageDao(): MessageDao
    abstract fun sessionDao(): SessionDao
    abstract fun userDao(): UserDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: AppRoomDatabase? = null

        fun getDatabase(context: Context): AppRoomDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppRoomDatabase::class.java,
                        "mojito_room"
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