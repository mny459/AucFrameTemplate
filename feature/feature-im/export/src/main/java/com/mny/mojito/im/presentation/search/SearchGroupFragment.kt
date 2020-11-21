package com.mny.mojito.im.presentation.search

import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.mny.mojito.base.BaseFragment
import com.mny.mojito.entension.observe
import com.mny.mojito.im.R
import com.mny.mojito.im.presentation.search.adapter.SearchGroupAdapter
import com.mny.mojito.im.widget.EmptyView
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * TODO
 */
@AndroidEntryPoint
class SearchGroupFragment : BaseFragment(R.layout.fragment_search_group) {
    private var mRvGroup: RecyclerView? = null
    private var mEmptyView: EmptyView? = null

    @Inject
    lateinit var mSearchAdapter: SearchGroupAdapter
    private val mViewModel by activityViewModels<SearchViewModel>()

    override fun initObserver() {
        super.initObserver()
        observe(mViewModel.stateLiveData, ViewStateObserver())
    }

    override fun initView(view: View) {
        super.initView(view)
        LogUtils.d("mSearchAdapter = $mSearchAdapter")
        mRvGroup = view.findViewById(R.id.rv_group)
        mRvGroup?.layoutManager = LinearLayoutManager(mActivity)
        mRvGroup?.adapter = mSearchAdapter

        mEmptyView = view.findViewById(R.id.empty_view)
        mEmptyView?.bind(mRvGroup)
    }

    override fun onFirstInit() {
        super.onFirstInit()
        // 发起首次搜索
        mViewModel.searchGroups("")
    }

    inner class ViewStateObserver : Observer<SearchViewModel.State> {
        override fun onChanged(state: SearchViewModel.State) {
            when {
                state.isLoading -> {
                    mEmptyView?.triggerLoading()
                }
                state.complete -> {
                    mSearchAdapter.data.clear()
                    if (state.groups.isNotEmpty()) {
                        mSearchAdapter.addData(state.groups)
                        mEmptyView?.triggerOk()
                    } else {
                        ToastUtils.showShort("${state.errorMsg}")
                    }
                    mEmptyView?.triggerOkOrEmpty(state.groups.isNotEmpty())
                }
                else -> {
                }
            }
        }
    }

    companion object {
        fun newInstance() = SearchGroupFragment()
    }
}