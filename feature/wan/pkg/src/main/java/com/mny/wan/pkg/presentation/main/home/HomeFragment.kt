package com.mny.wan.pkg.presentation.main.home

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.mny.wan.pkg.R
import com.mny.wan.pkg.base.BaseArticleFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class HomeFragment : BaseArticleFragment(R.layout.fragment_home) {
    private val mViewModel: HomeViewModel by viewModels()

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