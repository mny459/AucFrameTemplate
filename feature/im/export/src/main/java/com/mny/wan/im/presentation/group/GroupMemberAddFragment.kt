package com.mny.wan.im.presentation.group

import android.view.View
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.mny.mojito.base.BaseFragment
import com.mny.wan.im.R
import com.mny.wan.im.presentation.group.adapter.GroupMemberAddAdapter
import dagger.hilt.android.AndroidEntryPoint
import net.qiujuer.genius.ui.compat.UiCompat
import javax.inject.Inject

@AndroidEntryPoint
class GroupMemberAddFragment : BaseFragment(R.layout.fragment_group_member_add) {

    private var mRvMembers: RecyclerView? = null
    private var mToolbar: MaterialToolbar? = null
    private val mViewModel by activityViewModels<GroupMemberViewModel>()

    @Inject
    lateinit var mAdapter: GroupMemberAddAdapter
    override fun initView(view: View) {
        super.initView(view)
        mRvMembers = view.findViewById(R.id.rv_group_members)
        mToolbar = view.findViewById(R.id.toolbar)
        initToolbar()
        initRecycler()
    }

    private fun initRecycler() {
        mRvMembers?.layoutManager = LinearLayoutManager(context)
        mRvMembers?.adapter = mAdapter
    }

    private fun initToolbar() {
        mToolbar?.inflateMenu(R.menu.group_create)
        mToolbar?.setOnMenuItemClickListener {
            val result = if (it.itemId == R.id.action_create) {
                mViewModel.submit()
                true
            } else false
            result
        }
        val item = mToolbar?.menu?.findItem(R.id.action_create)
        item?.apply {
            var drawable = icon
            drawable = DrawableCompat.wrap(drawable)
            DrawableCompat.setTint(drawable, UiCompat.getColor(resources, R.color.textPrimary))
            icon = drawable
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = GroupMemberAddFragment()
    }
}