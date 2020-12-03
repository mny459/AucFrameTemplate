package com.mny.wan.pkg.presentation.main.home

import android.graphics.Color
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ConcatAdapter
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.LogUtils
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
    private var mBarView: View? = null
    override fun initView(view: View) {
        super.initView(view)
        LogUtils.d("initView ============== ")
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
    }

    override fun initArticleObserver() {
        LogUtils.d("initArticleObserver ============== ")
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
//        mBarView?.apply {
//            BarUtils.setStatusBarLightMode(this, Color.argb(0, 0, 0, 0))
//            BarUtils.setStatusBarColor(this, Color.argb(0, 0, 0, 0))
//        }
        enterFullScreen()
    }

    override fun onPause() {
        super.onPause()
        quitFullScreen()
//        mActivity?.apply {
//            BarUtils.setStatusBarColor(this, ContextCompat.getColor(this, R.color.colorPrimary))
//        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = HomeFragment()
    }
}