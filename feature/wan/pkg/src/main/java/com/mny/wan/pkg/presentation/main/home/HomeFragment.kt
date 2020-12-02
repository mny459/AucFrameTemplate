package com.mny.wan.pkg.presentation.main.home

import android.os.Bundle
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
import com.mny.wan.pkg.presentation.adapter.BannerAdapter
import com.mny.wan.pkg.presentation.adapter.TopArticleAdapter
import com.mny.wan.pkg.presentation.main.wechat.article.WeChatArticleFragment
import com.mny.wan.pkg.presentation.mine.MineActivity
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
        observe(mViewModel.mBannerList, BannerObserver())
        observe(mViewModel.mTopArticles, TopArticleObserver())
    }

    override fun onFirstInit() {
        super.onFirstInit()
        mViewModel.loadData()
    }

    inner class BannerObserver : Observer<List<BeanBanner>> {
        override fun onChanged(t: List<BeanBanner>?) {
            t?.apply {
                mBannerAdapter.replaceBanners(this)
            }
        }
    }

    inner class TopArticleObserver : Observer<List<BeanArticle>> {
        override fun onChanged(t: List<BeanArticle>?) {
            t?.apply {
                mTopArticleAdapter.replaceTopArticles(this)
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = HomeFragment()
    }
}