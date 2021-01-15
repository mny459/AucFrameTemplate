package com.mny.wan.pkg.presentation.main.home

import android.view.View
import androidx.fragment.app.activityViewModels
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
import com.mny.wan.pkg.data.remote.model.BeanBanner
import com.mny.wan.pkg.presentation.adapter.BannerAdapter
import com.mny.wan.pkg.presentation.adapter.HomeArticleAdapter
import com.mny.wan.pkg.presentation.adapter.TopArticleAdapter
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
class HomeFragment : BaseFragment(R.layout.fragment_home) {
    private val mViewModel: HomeViewModel by activityViewModels()
    private val mAllAdapter: ConcatAdapter by lazy { ConcatAdapter() }
    private val mBannerAdapter: BannerAdapter by lazy { BannerAdapter() }
    private val mTopArticleAdapter: TopArticleAdapter by lazy { TopArticleAdapter() }
    private var mBarView: View? = null

    protected var mRvArticles: RecyclerView? = null
    protected var mRefresh: SwipeRefreshLayout? = null

    @Inject
    protected lateinit var mAdapter: HomeArticleAdapter
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

        mBarView = view.findViewById(R.id.barStatusImageViewFragmentFakeStatusBar)
        view.findViewById<FloatingActionButton>(R.id.floatSearch)
            ?.setOnClickListener {
                SearchActivity.show()
//                MineActivity.show()
            }
        mAllAdapter.addAdapter(0, mBannerAdapter)
        mAllAdapter.addAdapter(1, mTopArticleAdapter)
        mAllAdapter.addAdapter(2, mAdapter)
        mRvArticles?.adapter = mAllAdapter
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
        observe(mViewModel.mBannerList) {
            it?.apply {
                refreshBanner(this)
            }
        }

        observe(mViewModel.mTopArticles) {
            it?.apply {
                mTopArticleAdapter.replaceTopArticles(this)
            }
        }
    }

    private fun refreshBanner(banners: List<BeanBanner>) {
        mBannerAdapter.replaceBanners(banners)
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
        fun newInstance() = HomeFragment()
    }
}