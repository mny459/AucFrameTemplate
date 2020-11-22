package com.mny.wan.im.presentation.group.adapter

import android.widget.CheckBox
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.mny.wan.im.R
import com.mny.wan.im.domain.model.GroupMemberModel
import com.mny.wan.im.widget.PortraitView
import javax.inject.Inject

class GroupMemberAdapter @Inject constructor() : BaseQuickAdapter<GroupMemberModel, BaseViewHolder>(R.layout.cell_group_create_contact) {
    override fun convert(holder: BaseViewHolder, item: GroupMemberModel) {
        val ivPortraitView = holder.getView<PortraitView>(R.id.iv_portrait)
        ivPortraitView.apply {
            setup(Glide.with(this), item.portrait)
        }
        holder.setText(R.id.tv_name, item.name)
        val cbSelect = holder.getView<CheckBox>(R.id.cb_select)
    }
}