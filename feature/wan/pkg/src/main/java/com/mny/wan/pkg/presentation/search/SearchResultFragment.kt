package com.mny.wan.pkg.presentation.search

import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.mny.wan.pkg.R
import com.mny.wan.pkg.base.BaseArticleFragment
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest

class SearchResultFragment : BaseArticleFragment(R.layout.fragment_search_result) {
    private val mViewModel: SearchViewModel by activityViewModels()
    override fun initArticleObserver() {
        lifecycleScope.launchWhenCreated {
            @OptIn(ExperimentalCoroutinesApi::class)
            mViewModel.mArticleList.collectLatest {
                mAdapter.submitData(it)
            }
        }
    }

}