package com.mny.wan.im.presentation.message

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DiffUtil
import com.blankj.utilcode.util.LogUtils
import com.bumptech.glide.Glide
import com.google.android.material.appbar.AppBarLayout
import com.mny.wan.entension.observe
import com.mny.wan.im.R
import com.mny.wan.im.data.db.entity.GroupMember
import com.mny.wan.im.data.db.entity.Message
import com.mny.wan.im.data.factory.AccountManager
import com.mny.wan.im.extension.loadBgForCollapsingToolbarLayout
import com.mny.wan.im.presentation.group.GroupMemberActivity
import com.mny.wan.im.presentation.message.adapter.MessageAdapter
import com.mny.wan.im.presentation.personal.PersonalActivity
import com.mny.wan.im.utils.DiffUiDataCallback
import com.mny.wan.im.widget.PortraitView
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ChatGroupFragment : ChatFragment() {
    private var mPortraitView: PortraitView? = null
    private var mMemberMore: TextView? = null
    private var mHeader: ImageView? = null
    private var mLayMembers: LinearLayout? = null

    @Inject
    lateinit var mAdapter: MessageAdapter
    protected val mViewModel by activityViewModels<MessageViewModel>()
    override fun initObserver() {
        super.initObserver()
        observe(mViewModel.stateLiveData, ViewStateObserver())
    }

    override fun initView(view: View) {
        super.initView(view)
        mRvMsg?.adapter = mAdapter

        mPortraitView = view.findViewById(R.id.iv_portrait)
        mMemberMore = view.findViewById(R.id.tv_member_more)
        mLayMembers = view.findViewById(R.id.lay_members)
        mHeader = view.findViewById(R.id.iv_header)
        mCollapsingLayout?.apply {
            loadBgForCollapsingToolbarLayout(R.drawable.default_banner_group, this)
        }
    }


    override fun pushText(text: String) {
        mViewModel.pushText(text)
    }

    override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
        TODO("Not yet implemented")
    }

    override fun getHeaderLayoutId(): Int = R.layout.lay_chat_header_group

    fun showAdminOption(isAdmin: Boolean) {
        if (isAdmin) {
            mToolbar?.inflateMenu(R.menu.chat_group)
            mToolbar?.setOnMenuItemClickListener {
                if (it.itemId == R.id.action_add) {
                    // mReceiverId 就是群的Id
                    mActivity?.apply {
                        GroupMemberActivity.go(this, mViewModel.stateLiveData.value!!.receiverId)
                    }
                    true
                }
                false
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

    inner class GroupMembersObserver : Observer<List<GroupMember>> {
        override fun onChanged(members: List<GroupMember>?) {
            LogUtils.d("onChanged - ${members}")
            if (members == null || members.isEmpty()) return

            val inflater = LayoutInflater.from(context)
            for (member in members) {
                // 添加成员头像
                val p = inflater.inflate(R.layout.lay_chat_group_portrait, mLayMembers, false) as ImageView
                mLayMembers?.addView(p, 0)
                Glide.with(this@ChatGroupFragment)
                        .load(member.user?.portrait)
                        .placeholder(R.drawable.default_portrait)
                        .centerCrop()
                        .dontAnimate()
                        .into(p)
                // 个人界面信息查看
                p.setOnClickListener {
                    mActivity?.apply {
                        PersonalActivity.go(this, member.groupMember?.userId ?: "")
                    }
                }
            }
            val moreCount = members.size - 4
            // 更多的按钮
            if (moreCount > 0) {
                mMemberMore?.text = String.format("+%s", moreCount)
                mMemberMore?.setOnClickListener { // mReceiverId 就是群的Id
                    mActivity?.apply {
                        GroupMemberActivity.go(this,
                                mViewModel.stateLiveData.value?.group?.group?.serverId ?: "")
                    }
                }
            } else {
                mMemberMore?.visibility = View.GONE
            }

        }
    }

    inner class ViewStateObserver : Observer<MessageViewModel.State> {
        override fun onChanged(t: MessageViewModel.State?) {
            t?.group?.let { group ->
                // 对和你聊天的朋友的信息进行初始化操作
                mCollapsingLayout?.title = group.group.name
                mHeader?.apply {
                    Glide.with(this)
                            .load(group.group.picture)
                            .centerCrop()
                            .placeholder(R.drawable.default_banner_group)
                            .into(this)
                }
                val isAdmin: Boolean = TextUtils.equals(AccountManager.getUserId(), group.group.ownerUserId)
                showAdminOption(isAdmin)
                observe(mViewModel.mChatAllMessages, MessagesObserver())
                observe(mViewModel.mGroupMembers, GroupMembersObserver())

            }
        }
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

    companion object {
        @JvmStatic
        fun newInstance() =
                ChatGroupFragment()
    }


}