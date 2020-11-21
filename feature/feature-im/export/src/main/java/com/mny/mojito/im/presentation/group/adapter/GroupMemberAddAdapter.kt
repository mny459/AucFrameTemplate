package com.mny.mojito.im.presentation.group.adapter

import android.widget.CheckBox
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.mny.mojito.im.R
import com.mny.mojito.im.widget.PortraitView
import javax.inject.Inject

class GroupMemberAddAdapter @Inject constructor() : BaseQuickAdapter<GroupMember, BaseViewHolder>(R.layout.cell_group_create_contact) {
    private var mOnCheckChangedListener: ((member: GroupMember, isChecked: Boolean) -> Unit)? = null
    fun setOnCheckChangedListener(listener: ((member: GroupMember, isChecked: Boolean) -> Unit)) {
        this.mOnCheckChangedListener = listener
    }

    override fun convert(holder: BaseViewHolder, item: GroupMember) {
        val ivPortraitView = holder.getView<PortraitView>(R.id.iv_portrait)
        ivPortraitView.apply {
            setup(Glide.with(this), item.user.portrait)
        }
        holder.setText(R.id.tv_name, item.user.name)
        val cbSelect = holder.getView<CheckBox>(R.id.cb_select)
        cbSelect.isChecked = item.isSelected
        cbSelect.setOnCheckedChangeListener { _, isChecked ->
            mOnCheckChangedListener?.invoke(item, isChecked)
        }
    }
}