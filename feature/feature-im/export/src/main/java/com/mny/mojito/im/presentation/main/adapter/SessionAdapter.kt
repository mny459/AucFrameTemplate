package com.mny.mojito.im.presentation.main.adapter

import android.text.Spannable
import android.text.SpannableString
import android.text.TextUtils
import android.widget.TextView
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.mny.mojito.im.R
import com.mny.mojito.im.data.db.entity.Session
import com.mny.mojito.im.utils.DateTimeUtil
import com.mny.mojito.im.widget.PortraitView
import com.mny.mojito.im.widget.face.Face
import javax.inject.Inject

class SessionAdapter @Inject constructor() : BaseQuickAdapter<Session, BaseViewHolder>(R.layout.cell_chat_list) {
    override fun convert(holder: BaseViewHolder, item: Session) {
        val portraitView = holder.getView<PortraitView>(R.id.iv_portrait)
        portraitView.setup(Glide.with(portraitView), item.session.picture)
        holder.setText(R.id.tv_name, item.session.title)
        val tvContent = holder.getView<TextView>(R.id.tv_content)
        item.session.modifyAt?.apply {
            holder.setText(R.id.tv_time, DateTimeUtil.getSampleDate(this))
        }

        val str = item.session.content ?: ""
        val spannable: Spannable = SpannableString(str)
        // 解析表情
        Face.decode(tvContent, spannable, tvContent.textSize.toInt())
        // 把内容设置到布局上
        tvContent.text = spannable
    }
}