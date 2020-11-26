package com.mny.wan.pkg.presentation.main.project.article

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.mny.wan.base.BaseFragment
import com.mny.wan.pkg.R
import com.mny.wan.pkg.base.BaseArticleFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest



@AndroidEntryPoint
class ProjectArticleFragment : BaseArticleFragment(R.layout.fragment_project_article) {

    private var mCid: Int = 0
    private val mViewModel: ProjectArticleViewModel by activityViewModels()
    override fun initArticleObserver() {
        lifecycleScope.launchWhenCreated {
            @OptIn(ExperimentalCoroutinesApi::class)
            mViewModel.mArticleList.collectLatest {
                mAdapter.submitData(it)
            }
        }
    }

    override fun initArgs(bundle: Bundle?) {
        super.initArgs(bundle)
        bundle?.let {
            mCid = it.getInt(ARG_CID, 0)
        }
    }

    override fun initData(savedInstanceState: Bundle?) {
        super.initData(savedInstanceState)
        mViewModel.initCId(mCid)
        mViewModel.loadData()
    }

    companion object {
        private const val ARG_CID = "c_id"
        @JvmStatic
        fun newInstance(cid: Int) =
            ProjectArticleFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_CID, cid)
                }
            }
    }
}