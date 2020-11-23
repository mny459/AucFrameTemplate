package com.mny.wan.pkg.presentation.adapter.tag

import com.blankj.utilcode.util.LogUtils
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.provider.BaseItemProvider
import com.mny.wan.pkg.R
import com.mny.wan.pkg.data.remote.model.BeanArticle
import com.mny.wan.pkg.data.remote.model.BeanHotKey
import com.mny.wan.pkg.data.remote.model.BeanMultiType
import com.mny.wan.pkg.data.remote.model.BeanSystemChildren
import org.jetbrains.anko.sdk27.coroutines.onClick

/**
 * @Author CaiRj
 * @Date 2019/10/18 17:10
 * @Desc
 */
class TagChildProvider : BaseItemProvider<BeanMultiType, BaseViewHolder>() {
    private var mOnChildClicked: ((data: Any) -> Unit)? = null
    override fun layout(): Int = R.layout.item_tag_child
    override fun viewType(): Int = BeanMultiType.TYPE_CHILD
    override fun convert(helper: BaseViewHolder, data: BeanMultiType?, position: Int) {
        data?.data?.apply {
            when (this) {
                is BeanSystemChildren -> helper.setText(R.id.tvTagChild, name)
                is BeanArticle -> helper.setText(R.id.tvTagChild, title)
                is BeanHotKey -> helper.setText(R.id.tvTagChild, name)
                else -> {

                }
            }
            helper.itemView.onClick {
                mOnChildClicked?.invoke(data.data)
            }
        }

    }

    fun setOnChildClickListener(onChildClickListener: ((data: Any) -> Unit)?) {
        this.mOnChildClicked = onChildClickListener
    }
}