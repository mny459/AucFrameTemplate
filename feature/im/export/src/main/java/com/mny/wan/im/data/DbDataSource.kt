package com.mny.wan.im.data

import com.mny.wan.im.data.DataSource.SucceedCallback

/**
 * 基础的数据库数据源接口定义
 *
 */
interface DbDataSource<Data> : DataSource {
    /**
     * 有一个基本的数据源加载方法
     *
     * @param callback 传递一个callback回调，一般回调到Presenter
     */
    fun load(callback: SucceedCallback<List<Data>>)
}