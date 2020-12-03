package com.mny.wan.pkg.presentation.main.qa

import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.mny.wan.pkg.R
import com.mny.wan.pkg.base.BaseArticleFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class QAFragment : BaseArticleFragment(R.layout.fragment_q_a) {

    private val mViewModel: QAViewModel by activityViewModels()

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

    companion object {
        @JvmStatic
        fun newInstance() = QAFragment()
    }
}