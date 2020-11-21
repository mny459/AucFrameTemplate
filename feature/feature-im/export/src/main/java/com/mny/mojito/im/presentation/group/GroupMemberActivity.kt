package com.mny.mojito.im.presentation.group

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mny.mojito.entension.observe
import com.mny.mojito.im.R
import com.mny.mojito.im.base.BaseToolbarActivity
import com.mny.mojito.im.presentation.group.adapter.GroupMember
import com.mny.mojito.im.presentation.group.adapter.GroupMemberAddAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class GroupMemberActivity : BaseToolbarActivity() {
    private var mRvGroupMembers: RecyclerView? = null
    private val mViewModel by viewModels<GroupMemberViewModel>()

    @Inject
    lateinit var mAdapter: GroupMemberAddAdapter
    override fun initArgs(bundle: Bundle?, savedInstanceState: Bundle?): Boolean {
        val groupId = bundle?.getString(KEY_GROUP_ID) ?: ""
        val isAdmin = bundle?.getBoolean(KEY_GROUP_ADMIN) ?: false
        mViewModel.init(groupId, isAdmin)
        return !TextUtils.isEmpty(groupId)
    }

    override fun layoutId(savedInstanceState: Bundle?): Int = R.layout.activity_group_member
    override fun initObserver() {
        super.initObserver()
        observe(mViewModel.mMembers, GroupMemberObserver())
    }

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        setTitle(R.string.title_member_list)
        mRvGroupMembers = findViewById(R.id.rv_group_members)
        mRvGroupMembers?.apply {
            layoutManager = LinearLayoutManager(mActivity)
            adapter = mAdapter
        }
    }

    inner class GroupMemberObserver : Observer<List<GroupMember>> {
        override fun onChanged(t: List<GroupMember>?) {
            t?.apply {
                if (isNotEmpty()) {
                    mAdapter.data.clear()
                    mAdapter.data.addAll(this)
                }
            }
        }
    }

    companion object {
        private const val KEY_GROUP_ID = "KEY_GROUP_ID"
        private const val KEY_GROUP_ADMIN = "KEY_GROUP_ADMIN"
        fun go(context: Context, groupId: String, isAdmin: Boolean = false) {
            if (TextUtils.isEmpty(groupId)) return
            val intent = Intent(context, GroupMemberActivity::class.java)
            intent.putExtra(KEY_GROUP_ID, groupId)
            intent.putExtra(KEY_GROUP_ADMIN, isAdmin)
            context.startActivity(intent)
        }
    }
}