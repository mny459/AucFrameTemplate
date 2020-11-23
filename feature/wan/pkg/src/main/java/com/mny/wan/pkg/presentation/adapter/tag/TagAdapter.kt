package com.mny.wan.pkg.presentation.adapter.tag

import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.MultipleItemRvAdapter
import com.mny.wan.pkg.data.remote.model.BeanMultiType

/**
 * @Author CaiRj
 * @Date 2019/10/18 16:38
 * @Desc
 */
class TagAdapter(data: List<BeanMultiType>, var onChildClickListener: ((data: Any) -> Unit)? = null) :
    MultipleItemRvAdapter<BeanMultiType, BaseViewHolder>(data) {
    init {
        finishInitialize()
    }

    override fun registerItemProvider() {
        val childProvider = TagChildProvider()
        childProvider.setOnChildClickListener(onChildClickListener)
        mProviderDelegate.registerProvider(TagParentProvider())
        mProviderDelegate.registerProvider(childProvider)
    }

    override fun getViewType(t: BeanMultiType?): Int = t?.type ?: BeanMultiType.TYPE_PARENT

}