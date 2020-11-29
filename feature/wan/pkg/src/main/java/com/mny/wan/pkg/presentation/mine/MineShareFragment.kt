package com.mny.wan.pkg.presentation.mine

import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.mny.wan.pkg.R
import com.mny.wan.pkg.base.BaseArticleFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class MineShareFragment : BaseArticleFragment(R.layout.fragment_mine_share) {
    private val mViewModel by activityViewModels<MineShareViewModel>()

    override fun initArticleObserver() {
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
}