package com.mny.wan.pkg.presentation.adapter.tag

import com.chad.library.adapter.base.provider.BaseItemProvider
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.mny.wan.pkg.R
import com.mny.wan.pkg.data.remote.model.BeanArticle
import com.mny.wan.pkg.data.remote.model.BeanHotKey
import com.mny.wan.pkg.data.remote.model.BeanMultiType
import com.mny.wan.pkg.data.remote.model.BeanSystemChildren

/**
 * @Author CaiRj
 * @Date 2019/10/18 17:10
 * @Desc
 */
class TagChildProvider : BaseItemProvider<BeanMultiType>() {
    private var mOnChildClicked: ((data: Any) -> Unit)? = null
    fun setOnChildClickListener(onChildClickListener: ((data: Any) -> Unit)?) {
        this.mOnChildClicked = onChildClickListener
    }

    override val itemViewType: Int
        get() = BeanMultiType.TYPE_CHILD
    override val layoutId: Int
        get() = R.layout.cell_tag_child

    override fun convert(helper: BaseViewHolder, data: BeanMultiType) {
        data?.data?.apply {
            when (this) {
                is BeanSystemChildren -> helper.setText(R.id.tvTagChild, name)
                is BeanArticle -> helper.setText(R.id.tvTagChild, title)
                is BeanHotKey -> helper.setText(R.id.tvTagChild, name)
                else -> {

                }
            }
            helper.itemView.setOnClickListener {
                mOnChildClicked?.invoke(data.data)
            }
        }
    }
}