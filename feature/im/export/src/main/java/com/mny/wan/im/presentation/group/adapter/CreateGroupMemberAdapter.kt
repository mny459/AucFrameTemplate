package com.mny.wan.im.presentation.group.adapter

import android.widget.CheckBox
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.mny.wan.im.R
import com.mny.wan.im.widget.PortraitView
import javax.inject.Inject

class CreateGroupMemberAdapter @Inject constructor() : BaseQuickAdapter<GroupMember, BaseViewHolder>(R.layout.cell_group_create_contact) {
    private var mOnCheckChangedListener: ((member: GroupMember, isChecked: Boolean) -> Unit)? = null
    fun setOnCheckChangedListener(listener: ((member: GroupMember, isChecked: Boolean) -> Unit)) {
        this.mOnCheckChangedListener = listener
    }

    override fun convert(holder: BaseViewHolder, item: GroupMember) {
        holder.setText(R.id.tv_name, item.user.name)
        val portraitView = holder.getView<PortraitView>(R.id.iv_portrait)
        portraitView.setup(Glide.with(portraitView), item.user.portrait)
        val cbSelect = holder.getView<CheckBox>(R.id.cb_select)
        cbSelect.isChecked = item.isSelected
        cbSelect.setOnCheckedChangeListener { _, isChecked ->
            mOnCheckChangedListener?.invoke(item, isChecked)
        }
    }
}