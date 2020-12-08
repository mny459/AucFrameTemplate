package com.mny.wan.im.presentation.search

import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.LogUtils
import com.mny.mojito.base.BaseFragment
import com.mny.wan.entension.observe
import com.mny.wan.im.R
import com.mny.wan.im.presentation.search.adapter.SearchAdapter
import com.mny.wan.im.widget.EmptyView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@AndroidEntryPoint
class SearchUserFragment : BaseFragment(R.layout.fragment_search_user) {
    private var mRvUser: RecyclerView? = null
    private var mEmptyView: EmptyView? = null

    //    @Inject
//    lateinit var mSearchAdapter: SearchUserAdapter
    @Inject
    lateinit var mSearchAdapter: SearchAdapter
    private val mViewModel by activityViewModels<SearchViewModel>()

    override fun initObserver() {
        super.initObserver()
        observe(mViewModel.stateLiveData, ViewStateObserver())
    }

    override fun initView(view: View) {
        super.initView(view)

        mRvUser = view.findViewById(R.id.rv_user)
        mRvUser?.layoutManager = LinearLayoutManager(mActivity)
        mRvUser?.adapter = mSearchAdapter

        mEmptyView = view.findViewById(R.id.empty_view)
        mEmptyView?.bind(mRvUser)
        mEmptyView?.triggerOk()

        lifecycleScope.launchWhenCreated {
            @OptIn(ExperimentalCoroutinesApi::class)
            mViewModel.mUsers.collectLatest {
                LogUtils.d("=============$it")
                mSearchAdapter.submitData(it)
            }
        }
    }

    override fun onFirstInit() {
        super.onFirstInit()
        // 发起首次搜索
        mViewModel.searchUsers("")
    }

    inner class ViewStateObserver : Observer<SearchViewModel.State> {
        override fun onChanged(state: SearchViewModel.State) {
            when {
                state.isLoading -> {
//                    mEmptyView?.triggerLoading()
                }
                state.complete -> {
//                    mSearchAdapter.data.clear()
//                    if (state.users.isNotEmpty()) {
//                        mSearchAdapter.addData(state.users)
//                    }
//                    mEmptyView?.triggerOkOrEmpty(state.users.isNotEmpty())
                }
                else -> {
                }
            }
        }
    }

    companion object {
        fun newInstance() = SearchUserFragment()
    }
}