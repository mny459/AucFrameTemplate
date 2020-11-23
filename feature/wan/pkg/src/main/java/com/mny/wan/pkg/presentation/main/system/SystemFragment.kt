package com.mny.wan.pkg.presentation.main.system

import androidx.fragment.app.Fragment
import android.view.View
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.mny.wan.base.BaseFragment
import com.mny.wan.pkg.R
import com.mny.wan.pkg.presentation.adapter.CommonFragmentAdapter
import com.mny.wan.pkg.presentation.main.system.share.ShareFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SystemFragment : BaseFragment(R.layout.fragment_system) {
    private var mTabLayout: TabLayout? = null
    private var mViewPage: ViewPager2? = null

    private val mFragments = mutableListOf<Fragment>()
    private lateinit var mVpAdapter: CommonFragmentAdapter

    override fun initView(view: View) {
        super.initView(view)
        mFragments.add(SystemTagFragment.newInstance(SystemTagFragment.TAG_SYSTEM))
        mFragments.add(SystemTagFragment.newInstance(SystemTagFragment.TAG_NAV))
        mFragments.add(ShareFragment.newInstance())

        mTabLayout = view.findViewById(R.id.tab_layout)
        mViewPage = view.findViewById(R.id.view_pager)
        mTabLayout?.apply {
            mTabLayout?.tabMode = TabLayout.MODE_FIXED
            //为TabLayout添加Tab选择事件监听
            addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab) {
                    //当标签被选择时回调
                }

                override fun onTabUnselected(tab: TabLayout.Tab) {
                    //当标签从选择变为非选择时回调
                }

                override fun onTabReselected(tab: TabLayout.Tab) {
                    //当标签被重新选择时回调
                }
            })
            mVpAdapter = CommonFragmentAdapter(this@SystemFragment, mFragments)
            mViewPage?.adapter = mVpAdapter
            TabLayoutMediator(this, mViewPage!!) { tab, position ->
                tab.text = when (position) {
                    0 -> "体系"
                    1 -> "导航"
                    2 -> "广场"
                    else -> ""
                }
            }.attach()
        }

    }
}