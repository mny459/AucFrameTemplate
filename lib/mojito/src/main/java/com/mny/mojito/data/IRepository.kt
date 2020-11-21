package com.mny.mojito.data

import androidx.room.RoomDatabase

/**
 *@author mny on 2020/5/17.
 *        Email：mny9@outlook.com
 *        Desc: Repository 应该包含三种数据存储方式：1. 网络 2. 数据库 3. KV(SP或MMKV)
 */
interface IRepository {
    fun <T> obtainRetrofitService(serviceClass: Class<T>): T
    fun <T : RoomDatabase> obtainRoom(roomClass: Class<T>): T
}