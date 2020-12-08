package com.mny.wan.pkg.base

import android.view.View
import androidx.annotation.LayoutRes
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.blankj.utilcode.util.LogUtils
import com.mny.mojito.base.BaseFragment
import com.mny.wan.pkg.R
import com.mny.wan.pkg.presentation.adapter.ArticleAdapter
import com.mny.wan.pkg.widget.loadstate.ArticleLoadStateAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter
import javax.inject.Inject

abstract class BaseArticleFragment(@LayoutRes contentLayoutId: Int) :
    BaseFragment(contentLayoutId) {
    protected var mRvArticles: RecyclerView? = null
    protected var mRefresh: SwipeRefreshLayout? = null

    @Inject
    protected lateinit var mAdapter: ArticleAdapter

    override fun initObserver() {
        super.initObserver()
        initArticleObserver()
    }

    override fun initView(view: View) {
        super.initView(view)
//        mAdapter = ArticleAdapter()
        lifecycleScope
        mRvArticles = view.findViewById(R.id.rv_articles)
        mRefresh = view.findViewById(R.id.refresh)
        mRefresh?.setOnRefreshListener {
            mAdapter.refresh()
        }

        mRvArticles?.apply {
            mAdapter.viewLifecycleOwner = this@BaseArticleFragment
            adapter = mAdapter.withLoadStateFooter(ArticleLoadStateAdapter(mAdapter))
        }

        lifecycleScope.launchWhenCreated {
            @OptIn(ExperimentalCoroutinesApi::class)
            mAdapter.loadStateFlow.collectLatest { loadStates ->
                LogUtils.d("Adapter 加载状态 loadStates = $loadStates")
                mRefresh?.isRefreshing = loadStates.refresh is LoadState.Loading
            }
        }

        lifecycleScope.launchWhenCreated {
            @OptIn(FlowPreview::class)
            mAdapter.loadStateFlow
                // Only emit when REFRESH LoadState for RemoteMediator changes.
                .distinctUntilChangedBy { it.refresh }
                // Only react to cases where Remote REFRESH completes i.e., NotLoading.
                .filter { it.refresh is LoadState.NotLoading }
                .collect { mRvArticles?.scrollToPosition(0) }
        }
    }

    //   lifecycleScope.launchWhenCreated {
    //            @OptIn(ExperimentalCoroutinesApi::class)
    //            mViewModel.mArticleList.collectLatest {
    //                mAdapter.submitData(it)
    //            }
    //        }
    abstract fun initArticleObserver()

    override fun onDestroyView() {
        mRefresh = null
        mRvArticles?.adapter = null
        mRvArticles = null
        super.onDestroyView()
    }
}