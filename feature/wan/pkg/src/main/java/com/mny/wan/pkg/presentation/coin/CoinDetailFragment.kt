package com.mny.wan.pkg.presentation.coin

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.blankj.utilcode.util.LogUtils
import com.mny.mojito.base.BaseFragment
import com.mny.wan.pkg.R
import com.mny.wan.pkg.presentation.adapter.CoinDetailAdapter
import com.mny.wan.pkg.widget.loadstate.CoinDetailLoadStateAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter

@AndroidEntryPoint
class CoinDetailFragment : BaseFragment(R.layout.fragment_coin_detail) {
    private val mViewModel: CoinDetailViewModel by viewModels()
    private var mRvCoins: RecyclerView? = null
    private var mRefresh: SwipeRefreshLayout? = null
    private lateinit var mAdapter: CoinDetailAdapter

    override fun initView(view: View) {
        super.initView(view)
        mAdapter = CoinDetailAdapter()
        mRvCoins = view.findViewById(R.id.rv_coin_detail)
        mRefresh = view.findViewById(R.id.refresh)
        mRefresh?.setOnRefreshListener {
            mAdapter.refresh()
        }

        mRvCoins?.apply {
            adapter = mAdapter.withLoadStateFooter(CoinDetailLoadStateAdapter(mAdapter))
        }
        lifecycleScope.launchWhenCreated {
            @OptIn(ExperimentalCoroutinesApi::class)
            mViewModel.mCoinDetails.collectLatest {
                mAdapter.submitData(it)
            }
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
                .collect { mRvCoins?.scrollToPosition(0) }
        }
    }

    override fun initData(savedInstanceState: Bundle?) {
        super.initData(savedInstanceState)
        mViewModel.loadData()
    }

}