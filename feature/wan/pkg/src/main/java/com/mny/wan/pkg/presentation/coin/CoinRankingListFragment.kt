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
import com.mny.wan.pkg.base.BaseBindingFragment
import com.mny.wan.pkg.databinding.FragmentCoinRankingListBinding
import com.mny.wan.pkg.presentation.adapter.CoinRankAdapter
import com.mny.wan.pkg.widget.loadstate.CoinRankLoadStateAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter

@AndroidEntryPoint
class CoinRankingListFragment : BaseBindingFragment<FragmentCoinRankingListBinding>() {
    private val mViewModel: CoinRankViewModel by viewModels()
    private lateinit var mAdapter: CoinRankAdapter

    override fun initView(view: View) {
        super.initView(view)
        mAdapter = CoinRankAdapter()
        mBinding.toolbar.setNavigationOnClickListener {
            mActivity?.onBackPressed()
        }
        mBinding.refresh.setOnRefreshListener {
            mAdapter.refresh()
        }

        mBinding.rvCoinRank.apply {
            adapter = mAdapter.withLoadStateFooter(CoinRankLoadStateAdapter(mAdapter))
        }
        lifecycleScope.launchWhenCreated {
            @OptIn(ExperimentalCoroutinesApi::class)
            mViewModel.mCoinRank.collectLatest {
                mAdapter.submitData(it)
            }
        }
        lifecycleScope.launchWhenCreated {
            @OptIn(ExperimentalCoroutinesApi::class)
            mAdapter.loadStateFlow.collectLatest { loadStates ->
                LogUtils.d("Adapter 加载状态 loadStates = $loadStates")
                mBinding.refresh.isRefreshing = loadStates.refresh is LoadState.Loading
            }
        }

        lifecycleScope.launchWhenCreated {
            @OptIn(FlowPreview::class)
            mAdapter.loadStateFlow
                // Only emit when REFRESH LoadState for RemoteMediator changes.
                .distinctUntilChangedBy { it.refresh }
                // Only react to cases where Remote REFRESH completes i.e., NotLoading.
                .filter { it.refresh is LoadState.NotLoading }
                .collect { mBinding.rvCoinRank.scrollToPosition(0) }
        }
    }

    override fun initData(savedInstanceState: Bundle?) {
        super.initData(savedInstanceState)
        mViewModel.loadData()
    }

}