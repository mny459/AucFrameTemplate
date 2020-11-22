package com.mny.wan.im.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.mny.wan.im.utils.DiffUiDataCallback
import java.util.*

@Entity(tableName = "user", indices = [Index("serverId", unique = true)])
data class UserEntity(
        @ColumnInfo(name = "serverId")// 主键
        @SerializedName(value = "id")
        var serverId: String = "",
        @ColumnInfo
        var name: String = "",
        @ColumnInfo
        var phone: String? = null,
        @ColumnInfo
        var portrait: String? = null,
        @ColumnInfo
        var desc: String? = null,
        @ColumnInfo
        var sex: Int = SEX_MAN,
        @ColumnInfo
        var alias: String? = null, // 我对某人的备注信息，也应该写入到数据库中
        @ColumnInfo
        var follows: Int = 0,// 用户关注人的数量
        @ColumnInfo
        var following: Int = 0, // 用户粉丝的数量
        @ColumnInfo
        var isFollow: Boolean = false,// 我与当前User的关系状态，是否已经关注了这个人
        @ColumnInfo
        var modifyAt: Date? = null // 时间字段
) : DiffUiDataCallback.UiDataDiffer<UserEntity> {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "dbId")
    var dbId: Long = 0

    companion object {
        const val SEX_MAN = 1
        const val SEX_WOMAN = 2

    }

    /**
     * 主要关注Id即可
     */
    override fun isSame(old: UserEntity): Boolean = this === old || serverId == old.serverId

    override fun isUiContentSame(old: UserEntity): Boolean = this === old || (name == old.name
            && portrait == old.portrait
            && sex == old.sex
            && isFollow == old.isFollow)
}
