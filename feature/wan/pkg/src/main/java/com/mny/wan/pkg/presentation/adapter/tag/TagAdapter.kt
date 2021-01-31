package com.mny.wan.pkg.presentation.adapter.tag

import com.chad.library.adapter.base.BaseProviderMultiAdapter
import com.mny.wan.pkg.data.remote.model.BeanMultiType

/**
 * @Author CaiRj
 * @Date 2019/10/18 16:38
 * @Desc
 */
class TagAdapter(
    data: MutableList<BeanMultiType>,
    var onChildClickListener: ((data: Any) -> Unit)? = null
) :
    BaseProviderMultiAdapter<BeanMultiType>(data) {
    init {
        val childProvider = TagChildProvider()
        childProvider.setOnChildClickListener(onChildClickListener)
        addItemProvider(TagParentProvider())
        addItemProvider(childProvider)

    }

    override fun getItemType(data: List<BeanMultiType>, position: Int): Int {
        return data[position].type
    }

}