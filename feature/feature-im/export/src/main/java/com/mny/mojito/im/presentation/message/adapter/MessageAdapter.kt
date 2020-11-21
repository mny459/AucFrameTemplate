package com.mny.mojito.im.presentation.message.adapter

import android.text.Spannable
import android.text.SpannableString
import android.view.View
import android.widget.TextView
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseDelegateMultiAdapter
import com.chad.library.adapter.base.delegate.BaseMultiTypeDelegate
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.mny.mojito.im.R
import com.mny.mojito.im.data.db.entity.Message
import com.mny.mojito.im.widget.PortraitView
import com.mny.mojito.im.widget.face.Face
import net.qiujuer.genius.ui.Ui
import net.qiujuer.genius.ui.compat.UiCompat
import net.qiujuer.genius.ui.widget.Loading
import javax.inject.Inject

class MessageAdapter @Inject constructor() : BaseDelegateMultiAdapter<Message, BaseViewHolder>() {
    init {
        setMultiTypeDelegate(MessageDelegate())
    }

    override fun convert(holder: BaseViewHolder, message: Message) {
        val isSelf = message.isSelfMsg()
        val mLoading: Loading? = if (isSelf) holder.getView(R.id.loading) else null
        val mPortraitView: PortraitView? = holder.getView<PortraitView>(R.id.iv_portrait)
        mPortraitView?.apply {
            mPortraitView.setup(Glide.with(this), message.sender.portrait)
            setOnClickListener {
                if (mLoading != null) {
                    // TODO 重发
                }
            }
        }

        mLoading?.apply {
            // 当前布局应该是在右边
            val status: Int = message.message.status
            when (status) {
                Message.STATUS_DONE -> {
                    // 正常状态, 隐藏Loading
                    mLoading.stop()
                    mLoading.visibility = View.GONE
                }
                Message.STATUS_CREATED -> {
                    // 正在发送中的状态
                    mLoading.visibility = View.VISIBLE
                    mLoading.progress = 0f
                    mLoading.setForegroundColor(UiCompat.getColor(resources, R.color.colorAccent))
                    mLoading.start()
                }
                Message.STATUS_FAILED -> {
                    // 发送失败状态, 允许重新发送
                    mLoading.visibility = View.VISIBLE
                    mLoading.stop()
                    mLoading.progress = 1f
                    mLoading.setForegroundColor(UiCompat.getColor(resources, R.color.alertImportant))
                }
            }
            // 当状态是错误状态时才允许点击
            mPortraitView?.isEnabled = status == Message.STATUS_FAILED
        }

        when (holder.itemViewType) {
            Message.TYPE_STR, Message.TYPE_STR_RIGHT -> {
                convertText(holder, message)
            }
            Message.TYPE_AUDIO, Message.TYPE_AUDIO_RIGHT -> {
                convertAudio(holder, message)
            }
            Message.TYPE_PIC, Message.TYPE_PIC_RIGHT -> {
                convertPic(holder, message)
            }
            else -> {
                convertText(holder, message)
            }
        }
    }

    private fun convertText(holder: BaseViewHolder, message: Message) {
        val tvContent = holder.getView<TextView>(R.id.tv_content)
        val spannable: Spannable = SpannableString(message.message.content)
        // 解析表情
        Face.decode(tvContent, spannable, Ui.dipToPx(tvContent.resources, 20f).toInt())

        // 把内容设置到布局上

        // 把内容设置到布局上
        tvContent.setText(spannable)
    }

    private fun convertAudio(holder: BaseViewHolder, message: Message) {
        // TODO
    }

    private fun convertPic(holder: BaseViewHolder, message: Message) {
        // TODO
    }
}

class MessageDelegate : BaseMultiTypeDelegate<Message>() {
    init {
        addItemType(Message.TYPE_STR, R.layout.cell_chat_text_left)
        addItemType(Message.TYPE_STR_RIGHT, R.layout.cell_chat_text_right)
        addItemType(Message.TYPE_AUDIO, R.layout.cell_chat_audio_left)
        addItemType(Message.TYPE_AUDIO_RIGHT, R.layout.cell_chat_audio_right)
        addItemType(Message.TYPE_PIC, R.layout.cell_chat_pic_left)
        addItemType(Message.TYPE_PIC_RIGHT, R.layout.cell_chat_pic_right)
    }



    override fun getItemType(data: List<Message>, position: Int): Int {
        val message = data[position]
        val isSelf = message.isSelfMsg()
        return when (message.message.type) {
            Message.TYPE_STR -> {
                if (!isSelf) Message.TYPE_STR
                else Message.TYPE_STR_RIGHT
            }
            Message.TYPE_AUDIO -> {
                if (!isSelf) Message.TYPE_AUDIO
                else Message.TYPE_AUDIO_RIGHT
            }
            Message.TYPE_PIC -> {
                if (!isSelf) Message.TYPE_PIC
                else Message.TYPE_PIC_RIGHT
            }
            else -> {
                if (!isSelf) Message.TYPE_STR
                else Message.TYPE_STR_RIGHT
            }
        }
    }

}