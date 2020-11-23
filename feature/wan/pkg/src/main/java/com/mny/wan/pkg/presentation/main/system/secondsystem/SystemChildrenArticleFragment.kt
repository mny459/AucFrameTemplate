package com.mny.wan.pkg.presentation.main.system.secondsystem

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.mny.wan.pkg.R
import com.mny.wan.pkg.base.BaseArticleFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class SystemChildrenArticleFragment :
    BaseArticleFragment(R.layout.fragment_system_children_article) {
    private var mChildId = DEFAULT_CHILD_ID

    private val mViewModel: SystemChildrenArticleViewModel by viewModels()

    override fun initArgs(bundle: Bundle?) {
        super.initArgs(bundle)
        mChildId = bundle?.getInt(CHILD_ID, DEFAULT_CHILD_ID) ?: DEFAULT_CHILD_ID
    }

    override fun onFirstInit() {
        super.onFirstInit()
        mViewModel.initCId(mChildId)
        mViewModel.loadData()
    }

    companion object {
        const val CHILD_ID = "child_id"
        const val DEFAULT_CHILD_ID = -1
        fun newInstance(childId: Int): SystemChildrenArticleFragment {
            val fragment = SystemChildrenArticleFragment()
            val args = Bundle()
            args.putInt(CHILD_ID, childId)
            fragment.arguments = args
            return fragment
        }
    }

    override fun initArticleObserver() {
        lifecycleScope.launchWhenCreated {
            @OptIn(ExperimentalCoroutinesApi::class)
            mViewModel.mArticleList.collectLatest {
                mAdapter.submitData(it)
            }
        }
    }
}