package com.mny.mojito.im.presentation.message.adapter

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.mny.mojito.im.R
import com.mny.mojito.im.widget.face.Face

class FaceAdapter(data: MutableList<Face.Bean>) : BaseQuickAdapter<Face.Bean, BaseViewHolder>(R.layout.cell_face, data = data) {
    override fun convert(holder: BaseViewHolder, item: Face.Bean) {
        val ivFace = holder.getView<ImageView>(R.id.iv_face)
        ivFace?.apply {
            Glide.with(this)
                    .asBitmap()
                    .load(item.preview)
                    .format(DecodeFormat.PREFER_ARGB_8888) //设置解码格式8888，保证清晰度
                    .placeholder(R.drawable.default_face)
                    .into(this)
        }

    }
}