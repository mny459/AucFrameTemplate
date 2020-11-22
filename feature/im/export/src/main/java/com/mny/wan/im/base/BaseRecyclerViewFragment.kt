package com.mny.wan.im.base

import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.DiffUtil
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.mny.wan.base.BaseFragment
import com.mny.wan.im.utils.DiffUiDataCallback
import com.mny.wan.im.utils.DiffUiDataCallback.UiDataDiffer

abstract class BaseRecyclerViewFragment<DATA : UiDataDiffer<DATA>, ADAPTER : BaseQuickAdapter<DATA, out BaseViewHolder>>(@LayoutRes contentLayoutId: Int) : BaseFragment(contentLayoutId) {

    abstract fun getAdapter(): ADAPTER

    fun refresh(data: List<DATA>) {
        getAdapter().data.clear()
        getAdapter().data.addAll(data)
        // 尝试刷新界面
    }

    fun diff(oldList: List<DATA>, newList: List<DATA>) {
        // 进行数据对比
        val callback: DiffUtil.Callback = DiffUiDataCallback(oldList, newList)
        val result: DiffUtil.DiffResult = DiffUtil.calculateDiff(callback)
        // 在对比完成后进行数据的赋值
        getAdapter().data.clear()
        getAdapter().data.addAll(newList)
        // 尝试刷新界面
        result.dispatchUpdatesTo(getAdapter())
    }
}