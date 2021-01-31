package com.mny.wan.pkg.presentation.article

import android.view.View
import androidx.annotation.LayoutRes
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.LogUtils
import com.jeremyliao.liveeventbus.LiveEventBus
import com.mny.mojito.base.BaseFragment
import com.mny.mojito.entension.observe
import com.mny.mojito.event.BaseEvent
import com.mny.wan.pkg.R
import com.mny.wan.pkg.event.CollectEvent
import com.mny.wan.pkg.presentation.adapter.WanArticleAdapter
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import javax.inject.Inject

abstract class BaseArticleFragment(@LayoutRes contentLayoutId: Int) :
    BaseFragment(contentLayoutId) {
    protected var mRvArticles: RecyclerView? = null
    protected var mRefresh: SmartRefreshLayout? = null
    private var articleViewModel: BaseArticleViewModel<*, *>? = null
    protected val mArticleViewModel: BaseArticleViewModel<*, *> by lazy { articleViewModel!! }

    @Inject
    protected lateinit var mAdapter: WanArticleAdapter

    override fun initView(view: View) {
        super.initView(view)
        articleViewModel = initArticleViewModel()

        mRvArticles = view.findViewById(R.id.rv_articles)
        mRefresh = view.findViewById(R.id.refresh)
        mRefresh?.setOnRefreshListener {
            mArticleViewModel.refresh()
        }
        mRefresh?.setOnLoadMoreListener {
            mArticleViewModel.loadMore()
        }
        mRvArticles?.apply { adapter = mAdapter }
        mAdapter.setOnCollectClickListener { item, isCollect ->
            mArticleViewModel.collect(item.id, isCollect)
        }
    }

    override fun initObserver() {
        super.initObserver()
        observe(mArticleViewModel.pageState) {
            LogUtils.d("initObserver ${this.javaClass.name} ------ $it $mRefresh")
            if (it.refresh) mRefresh?.finishRefresh(0, it.success, it.loadNoMore)
            else mRefresh?.finishLoadMore(0, it.success, it.loadNoMore)
        }
        observe(mArticleViewModel.articles) {
            mAdapter.setDiffNewData(it)
        }
        LiveEventBus.get(CollectEvent::class.java.simpleName, CollectEvent::class.java)
            .observe(viewLifecycleOwner) { event ->
                val index = mAdapter.data.indexOfFirst { event.articleId == it.id }
                if (index != -1) {
                    mAdapter.notifyItemChanged(index, event.collect)
                }
            }
    }

    override fun onFirstInit() {
        super.onFirstInit()
        mRefresh?.autoRefresh()
    }

    abstract fun initArticleViewModel(): BaseArticleViewModel<*, *>

    override fun onDestroyView() {
        mRefresh = null
        mRvArticles?.adapter = null
        mRvArticles = null
        articleViewModel = null
        super.onDestroyView()
    }


}