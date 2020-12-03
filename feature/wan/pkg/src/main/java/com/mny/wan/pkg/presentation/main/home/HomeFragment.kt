package com.mny.wan.pkg.presentation.main.home

import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ConcatAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.mny.wan.entension.observe
import com.mny.wan.pkg.R
import com.mny.wan.pkg.base.BaseArticleFragment
import com.mny.wan.pkg.data.remote.model.BeanArticle
import com.mny.wan.pkg.data.remote.model.BeanBanner
import com.mny.wan.pkg.extension.enterFullScreen
import com.mny.wan.pkg.extension.quitFullScreen
import com.mny.wan.pkg.presentation.adapter.BannerAdapter
import com.mny.wan.pkg.presentation.adapter.TopArticleAdapter
import com.mny.wan.pkg.presentation.search.SearchActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class HomeFragment : BaseArticleFragment(R.layout.fragment_home) {
    private val mViewModel: HomeViewModel by activityViewModels()
    private val mAllAdapter: ConcatAdapter by lazy { ConcatAdapter() }
    private val mBannerAdapter: BannerAdapter by lazy { BannerAdapter() }
    private val mTopArticleAdapter: TopArticleAdapter by lazy { TopArticleAdapter() }
    override fun initView(view: View) {
        super.initView(view)
        view.findViewById<FloatingActionButton>(R.id.floatSearch)
            ?.setOnClickListener {
                SearchActivity.show()
//                MineActivity.show()
            }
        mAllAdapter.addAdapter(0, mBannerAdapter)
        mAllAdapter.addAdapter(1, mTopArticleAdapter)
        mAllAdapter.addAdapter(2, mAdapter)
        mRvArticles?.adapter = mAllAdapter
    }

    override fun initArticleObserver() {
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

    override fun onResume() {
        super.onResume()
        enterFullScreen()
    }

    override fun onPause() {
        super.onPause()
        quitFullScreen()
    }

    companion object {
        @JvmStatic
        fun newInstance() = HomeFragment()
    }
}