package com.mny.wan.pkg.presentation.adapter.tag

import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.provider.BaseItemProvider
import com.mny.wan.pkg.R
import com.mny.wan.pkg.data.remote.model.BeanMultiType
import com.mny.wan.pkg.data.remote.model.BeanNav
import com.mny.wan.pkg.data.remote.model.BeanSystemParent

/**
 * @Author CaiRj
 * @Date 2019/10/18 17:10
 * @Desc
 */
class TagParentProvider : BaseItemProvider<BeanMultiType, BaseViewHolder>() {
    override fun layout(): Int = R.layout.cell_tag_parent

    override fun viewType(): Int = BeanMultiType.TYPE_PARENT
    override fun convert(helper: BaseViewHolder, data: BeanMultiType?, position: Int) {
        data?.data?.apply {
            when (this) {
                is BeanSystemParent -> helper.setText(R.id.tvTagParent, name)
                is BeanNav -> helper.setText(R.id.tvTagParent, name)
                is String -> helper.setText(R.id.tvTagParent, this)
            }
        }
    }
}