package com.mny.wan.pkg.presentation.main.qa

import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.blankj.utilcode.util.LogUtils
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.mny.mojito.base.BaseFragment
import com.mny.mojito.entension.observe
import com.mny.wan.pkg.R
import com.mny.wan.pkg.base.BaseArticleFragment
import com.mny.wan.pkg.data.remote.model.BeanBanner
import com.mny.wan.pkg.presentation.adapter.BannerAdapter
import com.mny.wan.pkg.presentation.adapter.HomeArticleAdapter
import com.mny.wan.pkg.presentation.adapter.QAArticleAdapter
import com.mny.wan.pkg.presentation.adapter.TopArticleAdapter
import com.mny.wan.pkg.presentation.main.home.HomeViewModel
import com.mny.wan.pkg.presentation.search.SearchActivity
import com.mny.wan.pkg.widget.loadstate.ArticleLoadStateAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter
import javax.inject.Inject

@AndroidEntryPoint
class QAFragment : BaseFragment(R.layout.fragment_q_a) {

    private val mViewModel: QAViewModel by activityViewModels()

    protected var mRvArticles: RecyclerView? = null
    protected var mRefresh: SwipeRefreshLayout? = null

    @Inject
    protected lateinit var mAdapter: QAArticleAdapter
    override fun initView(view: View) {
        super.initView(view)
        mRvArticles = view.findViewById(R.id.rv_articles)
        mRefresh = view.findViewById(R.id.refresh)
        mRefresh?.setOnRefreshListener {
            mAdapter.refresh()
        }

        mRvArticles?.apply {
            mAdapter.viewLifecycleOwner = viewLifecycleOwner
            adapter = mAdapter.withLoadStateFooter(ArticleLoadStateAdapter { mAdapter.retry() })
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

        view.findViewById<FloatingActionButton>(R.id.floatSearch)
            ?.setOnClickListener {
                SearchActivity.show()
//                MineActivity.show()
            }
        mRvArticles?.adapter = mAdapter
        mAdapter.addLoadStateListener {
            LogUtils.d("addLoadStateListener\nappend = ${it.append}\nsource = ${it.source}\nmediator = ${it.mediator}")
        }
    }

    override fun initObserver() {
        super.initObserver()
        initArticleObserver()
    }


    fun initArticleObserver() {
        lifecycleScope.launchWhenCreated {
            @OptIn(ExperimentalCoroutinesApi::class)
            mViewModel.mArticleList.collectLatest {
                mAdapter.submitData(it)
            }
        }

    }

    override fun onFirstInit() {
        super.onFirstInit()
        mViewModel.loadData()
    }

    override fun onDestroyView() {
        mRefresh = null
        mRvArticles?.adapter = null
        mRvArticles = null
        super.onDestroyView()
    }

    companion object {
        @JvmStatic
        fun newInstance() = QAFragment()
    }
}