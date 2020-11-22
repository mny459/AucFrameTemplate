package com.mny.wan.im.presentation.main

import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.LogUtils
import com.mny.wan.base.BaseFragment
import com.mny.wan.entension.observe
import com.mny.wan.im.R
import com.mny.wan.im.data.db.entity.Group
import com.mny.wan.im.presentation.main.adapter.GroupAdapter
import com.mny.wan.im.presentation.message.MessageActivity
import com.mny.wan.im.presentation.viewmodel.GroupViewModel
import com.mny.wan.im.widget.EmptyView
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class GroupFragment : BaseFragment(R.layout.fragment_group) {

    private var mRvGroup: RecyclerView? = null
    private var mEmptyView: EmptyView? = null
    private var mRefresh: SmartRefreshLayout? = null
    private val mViewModel by viewModels<GroupViewModel>()

    @Inject
    lateinit var mAdapter: GroupAdapter

    override fun initObserver() {
        super.initObserver()
        observe(mViewModel.mGroups, GroupsObserver())
    }

    override fun initView(view: View) {
        super.initView(view)
        mRvGroup = view.findViewById(R.id.rv_group)
        mRvGroup?.apply {
            layoutManager = GridLayoutManager(mActivity, 2)
            adapter = mAdapter
        }

        mEmptyView = view.findViewById(R.id.empty_view)
        mRefresh = view.findViewById(R.id.refresh)
        mEmptyView?.bind(mRvGroup)
        mAdapter.setOnItemClickListener { _, _, position ->
            MessageActivity.go(mActivity!!, mAdapter.data[position])
        }

        mRefresh?.setOnRefreshListener {
            mViewModel.loadData()
            it.finishRefresh(1000)
        }
    }

    inner class GroupsObserver : Observer<List<Group>> {
        override fun onChanged(t: List<Group>?) {
            LogUtils.d("onChanged $t")
            t?.apply {
                mAdapter.data.clear()
                mAdapter.addData(this)
                mEmptyView?.triggerOkOrEmpty(!isEmpty())
            }
        }
    }

    companion object {
        fun newInstance() = GroupFragment()
    }
}