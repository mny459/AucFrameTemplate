package com.mny.wan.pkg.presentation.main

import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.viewpager2.widget.ViewPager2
import com.blankj.utilcode.util.LogUtils
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mny.mojito.base.BaseFragment
import com.mny.wan.pkg.R
import com.mny.wan.pkg.presentation.adapter.CommonFragmentAdapter
import com.mny.wan.pkg.presentation.main.home.HomeFragment
import com.mny.wan.pkg.presentation.main.project.ProjectFragment
import com.mny.wan.pkg.presentation.main.qa.QAFragment
import com.mny.wan.pkg.presentation.main.system.SystemFragment
import com.mny.wan.pkg.presentation.main.wechat.WeChatFragment

class MainTabFragment : BaseFragment(R.layout.fragment_main_tab) {
    private var mViewPager: ViewPager2? = null
    private var mBottomNavView: BottomNavigationView? = null
    private val mVpAdapter: CommonFragmentAdapter by lazy {
        CommonFragmentAdapter(
            this,
            mTabs.toMutableList()
        )
    }
    private val mTabs by lazy {
        listOf(
            HomeFragment.newInstance(),
            WeChatFragment.newInstance(),
            ProjectFragment.newInstance(),
            SystemFragment.newInstance(),
            QAFragment.newInstance(),
        )
    }


    override fun initView(view: View) {
        super.initView(view)
        mViewPager = view.findViewById(R.id.view_pager)
        mViewPager?.apply {
            adapter = mVpAdapter
            // 禁止 ViewPager 滑动切换
            isUserInputEnabled = false
        }
        mBottomNavView = view.findViewById(R.id.navigation)
        mBottomNavView?.apply {
            LogUtils.d("===================================")
            setOnNavigationItemSelectedListener { item ->
                LogUtils.d("setOnNavigationItemSelectedListener ${item.itemId}")
                when (item.itemId) {
                    R.id.homeFragment -> {
                        mViewPager?.setCurrentItem(0, true)
                    }
                    R.id.weChatFragment -> {
                        mViewPager?.setCurrentItem(1, true)
                    }
                    R.id.projectFragment -> {
                        mViewPager?.setCurrentItem(2, true)
                    }
                    R.id.systemFragment -> {
                        mViewPager?.setCurrentItem(3, true)
                    }
                    R.id.QAFragment -> {
                        mViewPager?.setCurrentItem(4, true)
                    }
                }
                true
            }
        }
        mViewPager?.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                mBottomNavView?.menu?.getItem(position)?.isChecked = true
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.navigation_items, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
}