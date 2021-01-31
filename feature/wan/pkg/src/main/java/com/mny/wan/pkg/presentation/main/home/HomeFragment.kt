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
import com.jeremyliao.liveeventbus.LiveEventBus
import com.mny.mojito.base.BaseFragment
import com.mny.mojito.entension.observe
import com.mny.wan.pkg.R
import com.mny.wan.pkg.data.remote.model.BeanBanner
import com.mny.wan.pkg.event.CollectEvent
import com.mny.wan.pkg.presentation.adapter.BannerAdapter
import com.mny.wan.pkg.presentation.adapter.HomeArticleAdapter
import com.mny.wan.pkg.presentation.adapter.TopArticleAdapter
import com.mny.wan.pkg.presentation.article.BaseArticleFragment
import com.mny.wan.pkg.presentation.article.BaseArticleViewModel
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
class HomeFragment : BaseArticleFragment(R.layout.fragment_home) {
    private val mViewModel: HomeViewModel by activityViewModels()
    private val mAllAdapter: ConcatAdapter by lazy { ConcatAdapter() }
    private val mBannerAdapter: BannerAdapter by lazy { BannerAdapter() }

    @Inject
    lateinit var mTopArticleAdapter: TopArticleAdapter
    private var mBarView: View? = null

    override fun initView(view: View) {
        super.initView(view)
        mBarView = view.findViewById(R.id.barStatusImageViewFragmentFakeStatusBar)
        view.findViewById<FloatingActionButton>(R.id.floatSearch)
            ?.setOnClickListener {
                SearchActivity.show()
            }
        mAllAdapter.addAdapter(0, mBannerAdapter)
        mAllAdapter.addAdapter(1, mTopArticleAdapter)
        mAllAdapter.addAdapter(2, mAdapter)
        mRvArticles?.adapter = mAllAdapter
        mTopArticleAdapter.setOnCollectClickListener { item, isCollect ->
            mArticleViewModel.collect(item.id, isCollect)
        }
    }

    override fun initObserver() {
        super.initObserver()
        initArticleObserver()
        LiveEventBus.get(CollectEvent::class.java.simpleName, CollectEvent::class.java)
            .observe(viewLifecycleOwner) { event ->
                val index = mTopArticleAdapter.data.indexOfFirst { event.articleId == it.id }
                if (index != -1) {
                    mTopArticleAdapter.notifyItemChanged(index, event.collect)
                }
            }
    }


    private fun initArticleObserver() {
        observe(mViewModel.mBannerList) {
            it?.apply {
                refreshBanner(this)
            }
        }

        observe(mViewModel.mTopArticles) {
            it?.apply {
                mTopArticleAdapter.setDiffNewData(this.toMutableList())
            }
        }
    }

    private fun refreshBanner(banners: List<BeanBanner>) {
        mBannerAdapter.replaceBanners(banners)
    }

    override fun onFirstInit() {
        mViewModel.loadData()
        super.onFirstInit()
    }

    override fun initArticleViewModel(): BaseArticleViewModel<*, *> = mViewModel

    companion object {
        @JvmStatic
        fun newInstance() = HomeFragment()
    }
}