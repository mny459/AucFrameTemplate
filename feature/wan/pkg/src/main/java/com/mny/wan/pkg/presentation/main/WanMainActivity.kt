package com.mny.wan.pkg.presentation.main

import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.LogUtils
import com.mny.wan.pkg.R
import com.mny.wan.pkg.base.BaseBindingActivity
import com.mny.wan.pkg.databinding.ActivityWanMainBinding
import com.mny.wan.pkg.extension.enterFullScreen
import com.mny.wan.pkg.presentation.adapter.CommonFragmentAdapterForActivity
import com.mny.wan.pkg.presentation.main.home.HomeFragment
import com.mny.wan.pkg.presentation.main.qa.QAFragment
import com.mny.wan.pkg.presentation.mine.MineFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WanMainActivity : BaseBindingActivity<ActivityWanMainBinding>() {

    private val mVpAdapter: CommonFragmentAdapterForActivity by lazy {
        CommonFragmentAdapterForActivity(
            this,
            mTabs.toMutableList()
        )
    }
    private val mTabs by lazy {
        listOf(
            HomeFragment.newInstance(),
            QAFragment.newInstance(),
            MineFragment.newInstance(),
        )
    }
    private val mTabIds by lazy {
        listOf(
            R.id.main_home_nav_graph,
            R.id.main_qa_nav_graph,
            R.id.main_mine_nav_graph,
        )
    }

    override fun initWindow(savedInstanceState: Bundle?) {
        super.initWindow(savedInstanceState)
        setTheme(R.style.AppTheme)
        enterFullScreen()
    }

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        mBinding.viewPager.adapter = mVpAdapter
        mBinding.viewPager.isUserInputEnabled = false
        mBinding.navigation.setOnNavigationItemSelectedListener { item ->
            LogUtils.d("setOnNavigationItemSelectedListener ${item.itemId} ${R.id.homeFragment} ${R.id.QAFragment} ${R.id.mineFragment}")
            val index = mTabIds.indexOf(item.itemId)
            if (index != -1) {
                mBinding.viewPager.setCurrentItem(index, false)
            }
            true
        }
        mBinding.viewPager?.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                mBinding.navigation?.menu?.getItem(position)?.isChecked = true
            }
        })
    }

    companion object {
        fun show() {
            ActivityUtils.startActivity(WanMainActivity::class.java)
        }
    }
}