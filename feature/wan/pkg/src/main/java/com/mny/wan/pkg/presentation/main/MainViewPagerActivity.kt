package com.mny.wan.pkg.presentation.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.mny.wan.pkg.R
import com.mny.wan.pkg.base.BaseBindingActivity
import com.mny.wan.pkg.databinding.ActivityMainViewPagerBinding
import com.mny.wan.pkg.presentation.adapter.CommonFragmentViewPagerAdapter
import com.mny.wan.pkg.presentation.main.home.HomeFragment
import com.mny.wan.pkg.presentation.main.qa.QAFragment
import com.mny.wan.pkg.presentation.main.system.SystemFragment
import com.mny.wan.pkg.presentation.mine.MineFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainViewPagerActivity : BaseBindingActivity<ActivityMainViewPagerBinding>() {

    private val mFragments = listOf<Fragment>(
        HomeFragment.newInstance(),
        QAFragment.newInstance(),
        SystemFragment.newInstance(),
        MineFragment.newInstance()
    )

    private lateinit var mVpAdapter: CommonFragmentViewPagerAdapter

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        mVpAdapter =
            CommonFragmentViewPagerAdapter(supportFragmentManager, mFragments)
        mBinding.viewPager.adapter = mVpAdapter
        mBinding.viewPager.offscreenPageLimit = 4
        mBinding.viewPager.setDisableScroll()
        mBinding.navigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.homeFragment -> {
                    mBinding.viewPager.currentItem = 0
                }
                R.id.QAFragment -> {
                    mBinding.viewPager.currentItem = 1
                }
                R.id.systemFragment -> {
                    mBinding.viewPager.currentItem = 2
                }
                R.id.mineFragment -> {
                    mBinding.viewPager.currentItem = 3
                }
                else -> {
                }
            }
            true
        }
    }
}