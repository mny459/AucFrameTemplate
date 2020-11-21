package com.mny.mojito.im.presentation.message

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import androidx.activity.viewModels
import androidx.navigation.Navigation
import com.blankj.utilcode.util.LogUtils
import com.mny.mojito.base.BaseActivity
import com.mny.mojito.im.R
import com.mny.mojito.im.data.db.entity.Group
import com.mny.mojito.im.data.db.entity.Message
import com.mny.mojito.im.data.db.entity.Session
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MessageActivity : BaseActivity() {
    private lateinit var mReceiverId: String
    private var mIsGroup: Boolean = false
    private val mViewModel by viewModels<MessageViewModel>()

    override fun initArgs(bundle: Bundle?, savedInstanceState: Bundle?): Boolean {
        mReceiverId = bundle?.getString(KEY_RECEIVER_ID, "") ?: ""
        mIsGroup = bundle?.getBoolean(KEY_RECEIVER_IS_GROUP) ?: false
        return !TextUtils.isEmpty(mReceiverId)
    }

    override fun layoutId(savedInstanceState: Bundle?): Int = R.layout.activity_message
    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        mViewModel.init(mReceiverId, if (mIsGroup) Message.RECEIVER_TYPE_GROUP else Message.RECEIVER_TYPE_NONE)
        title = ""
        val navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        // 默认是用户，群才做处理
        if (mIsGroup) {
            navController.navigate(R.id.chatGroupFragment)
        }
    }

    companion object {
        // 接收者Id，可以是群，也可以是人的Id
        private const val KEY_RECEIVER_ID = "KEY_RECEIVER_ID"

        // 是否是群
        private const val KEY_RECEIVER_IS_GROUP = "KEY_RECEIVER_IS_GROUP"
        fun go(context: Context, receiverId: String) {
            LogUtils.d("go - $receiverId")
            val intent = Intent(context, MessageActivity::class.java)
            intent.putExtra(KEY_RECEIVER_ID, receiverId)
            intent.putExtra(KEY_RECEIVER_IS_GROUP, false)
            context.startActivity(intent)
        }

        fun go(context: Context, session: Session) {
            val intent = Intent(context, MessageActivity::class.java)
            intent.putExtra(KEY_RECEIVER_ID, session.session.serverId)
            intent.putExtra(KEY_RECEIVER_IS_GROUP, session.session.receiverType == Message.RECEIVER_TYPE_GROUP)
            context.startActivity(intent)
        }

        fun go(context: Context, group: Group) {
            val intent = Intent(context, MessageActivity::class.java)
            intent.putExtra(KEY_RECEIVER_ID, group.group.serverId)
            intent.putExtra(KEY_RECEIVER_IS_GROUP, true)
            context.startActivity(intent)
        }
    }
}