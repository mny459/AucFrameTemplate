package com.mny.mojito.im.presentation.main.adapter

import android.app.Activity
import android.content.Context
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.mny.mojito.im.R
import com.mny.mojito.im.data.db.entity.UserEntity
import com.mny.mojito.im.presentation.message.MessageActivity
import com.mny.mojito.im.presentation.personal.PersonalActivity
import com.mny.mojito.im.widget.PortraitView
import dagger.hilt.android.qualifiers.ActivityContext
import javax.inject.Inject

class ContactAdapter @Inject constructor(@ActivityContext private val mActivity: Context) : BaseQuickAdapter<UserEntity, BaseViewHolder>(R.layout.cell_contact_list) {
    override fun convert(holder: BaseViewHolder, item: UserEntity) {
        val ivPortrait = holder.getView<PortraitView>(R.id.iv_portrait);
        ivPortrait.setup(Glide.with(ivPortrait), item.portrait)
        holder.setText(R.id.tv_name, item.name)
        holder.setText(R.id.tv_desc, item.desc)
        holder.itemView.setOnClickListener { _ ->
            MessageActivity.go(mActivity, item.serverId)
        }
        ivPortrait.setOnClickListener { _ ->
            PersonalActivity.go(mActivity as Activity, item.serverId)

        }
    }
}