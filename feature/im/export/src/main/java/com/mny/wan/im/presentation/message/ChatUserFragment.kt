package com.mny.wan.im.presentation.message

import android.view.MenuItem
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.google.android.material.appbar.AppBarLayout
import com.mny.wan.entension.observe
import com.mny.wan.im.R
import com.mny.wan.im.data.db.entity.Message
import com.mny.wan.im.extension.loadBgForCollapsingToolbarLayout
import com.mny.wan.im.presentation.message.adapter.MessageAdapter
import com.mny.wan.im.presentation.personal.PersonalActivity
import com.mny.wan.im.utils.DiffUiDataCallback
import com.mny.wan.im.widget.PortraitView
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ChatUserFragment : ChatFragment() {
    private var mPortraitView: PortraitView? = null
    private var mUserInfoMenu: MenuItem? = null

    @Inject
    lateinit var mAdapter: MessageAdapter
    protected val mViewModel by activityViewModels<MessageViewModel>()
    override fun initView(view: View) {
        super.initView(view)
        mRvMsg?.adapter = mAdapter

        mPortraitView = view.findViewById(R.id.iv_portrait)
        mCollapsingLayout?.apply {
            loadBgForCollapsingToolbarLayout(R.drawable.default_banner_chat, this)
        }
        mToolbar?.inflateMenu(R.menu.chat_user)
        mToolbar?.setOnMenuItemClickListener {
            if (it.itemId == R.id.action_person) {
                goCheckUserInfo()
            }
            false
        }
        mUserInfoMenu = mToolbar?.menu?.findItem(R.id.action_person)
    }

    override fun initObserver() {
        super.initObserver()
        observe(mViewModel.stateLiveData, ViewStateObserver())
    }

    override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
//        val view = mPortraitView
//        val menuItem = mUserInfoMenu
//
//        if (view == null || menuItem == null) return
//
//        if (verticalOffset == 0) {
//            // 完全展开
//            view.visibility = View.VISIBLE
//            view.scaleX = 1f
//            view.scaleY = 1f
//            view.alpha = 1f
//
//            // 隐藏菜单
//            menuItem.isVisible = false
//            menuItem.icon.alpha = 0
//        } else {
//            // abs 运算
//            val verticalOffsetAbs = abs(verticalOffset)
//            val totalScrollRange = appBarLayout.totalScrollRange
//            if (verticalOffsetAbs >= totalScrollRange) {
//                // 关闭状态
//                view.visibility = View.INVISIBLE
//                view.scaleX = 0f
//                view.scaleY = 0f
//                view.alpha = 0f
//
//                // 显示菜单
//                menuItem.isVisible = true
//                menuItem.icon.alpha = 255
//            } else {
//                // 中间状态
//                val progress = 1 - verticalOffsetAbs / totalScrollRange.toFloat()
//                view.visibility = View.VISIBLE
//                view.scaleX = progress
//                view.scaleY = progress
//                view.alpha = progress
//                // 和头像恰好相反
//                menuItem.isVisible = true
//                menuItem.icon.alpha = 255 - (255 * progress).toInt()
//            }
//        }
    }

    override fun getHeaderLayoutId(): Int = R.layout.lay_chat_header_user

    private fun goCheckUserInfo() {
        mActivity?.apply {
            PersonalActivity.go(this, mViewModel.stateLiveData.value?.receiverId ?: "")
        }
    }

    override fun pushText(text: String) {
        mViewModel.pushText(text)
    }

    inner class ViewStateObserver : Observer<MessageViewModel.State> {
        override fun onChanged(t: MessageViewModel.State?) {
            t?.user?.let { user ->
                // 对和你聊天的朋友的信息进行初始化操作
                mPortraitView?.apply {
                    setup(Glide.with(this), user.portrait)
                }
                mCollapsingLayout?.title = user.name
                observe(mViewModel.mChatAllMessages, MessagesObserver())
            }
        }
    }

    inner class MessagesObserver : Observer<List<Message>> {
        override fun onChanged(t: List<Message>?) {
            t?.apply {
                diff(mAdapter.data, this)
            }
        }

    }

    companion object {
        @JvmStatic
        fun newInstance() = ChatUserFragment()
    }

    fun diff(oldList: List<Message>, newList: List<Message>) {
        // 进行数据对比
        val callback: DiffUtil.Callback = DiffUiDataCallback(oldList, newList)
        val result: DiffUtil.DiffResult = DiffUtil.calculateDiff(callback)
        // 在对比完成后进行数据的赋值
        mAdapter.data.clear()
        mAdapter.data.addAll(newList)
        // 尝试刷新界面
        result.dispatchUpdatesTo(mAdapter)
    }
}