package com.mny.wan.pkg.presentation.main.project

import android.view.View
import androidx.fragment.app.Fragment
import com.mny.wan.base.BaseFragment
import com.mny.wan.pkg.R
import com.mny.wan.pkg.presentation.adapter.CommonFragmentAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProjectFragment : BaseFragment(R.layout.fragment_project) {
    private val mFragments = mutableListOf<Fragment>()
    private lateinit var mVpAdapter: CommonFragmentAdapter
    override fun initView(view: View) {
        super.initView(view)
    }
}