package com.mny.mojito.im.presentation.main.adapter

import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.mny.mojito.im.R
import com.mny.mojito.im.data.db.entity.Group
import com.mny.mojito.im.widget.PortraitView
import javax.inject.Inject

class GroupAdapter @Inject constructor() : BaseQuickAdapter<Group, BaseViewHolder>(R.layout.cell_group_list) {
    override fun convert(holder: BaseViewHolder, item: Group) {
        val ivPortraitView = holder.getView<PortraitView>(R.id.iv_portrait)
        ivPortraitView?.apply {
            setup(Glide.with(this),item.group.picture)
        }
        holder.setText(R.id.tv_name,item.group.name)
        holder.setText(R.id.tv_desc,item.group.desc)
        // TODO HOLDER
//        if (item.group.h)
//        if (group.holder != null && group.holder is String) {
//            mMember.setText(group.holder as String)
//        } else {
//            mMember.setText("")
//        }
    }
}