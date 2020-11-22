package com.mny.wan.im.presentation.main

import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mny.wan.base.BaseFragment
import com.mny.wan.entension.observe
import com.mny.wan.im.R
import com.mny.wan.im.data.db.entity.Session
import com.mny.wan.im.presentation.main.adapter.SessionAdapter
import com.mny.wan.im.presentation.message.MessageActivity
import com.mny.wan.im.presentation.viewmodel.SessionViewModel
import com.mny.wan.im.widget.EmptyView
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ActiveFragment : BaseFragment(R.layout.fragment_active) {
    private var mEmptyView: EmptyView? = null
    private var mRvSessions: RecyclerView? = null
    private var mRefresh: SmartRefreshLayout? = null

    @Inject
    lateinit var mAdapter: SessionAdapter

    private val mViewModel by viewModels<SessionViewModel>()
    override fun initObserver() {
        super.initObserver()
        observe(mViewModel.stateLiveData, ViewStateObserver())
        observe(mViewModel.mSessions, SessionObserver())
    }

    override fun initView(view: View) {
        super.initView(view)
        mEmptyView = view.findViewById(R.id.empty)
        mRvSessions = view.findViewById(R.id.rv_sessions)
        mRefresh = view.findViewById(R.id.refresh)
        mRvSessions?.layoutManager = LinearLayoutManager(mActivity)
        mRvSessions?.adapter = mAdapter

        mEmptyView?.bind(mRvSessions)

        mAdapter.setOnItemClickListener { adapter, view, position ->
            MessageActivity.go(mActivity!!, mAdapter.data[position])
        }

        mRefresh?.setOnRefreshListener {
            lifecycleScope.launch {
                mViewModel.loadData()
                it.finishRefresh(1000)
            }
        }
    }

    override fun onFirstInit() {
        super.onFirstInit()
        mViewModel.loadData()
    }

    inner class ViewStateObserver : Observer<SessionViewModel.State> {
        override fun onChanged(t: SessionViewModel.State?) {

        }
    }

    inner class SessionObserver : Observer<List<Session>> {
        override fun onChanged(t: List<Session>?) {
            t?.apply {
                mAdapter.data.clear()
                if (isNotEmpty()) {
                    mAdapter.addData(this)
                    mEmptyView?.triggerOk()
                } else {
                    mEmptyView?.triggerEmpty()
                }
            }
        }

    }

    companion object {
        fun newInstance() = ActiveFragment()
    }
}