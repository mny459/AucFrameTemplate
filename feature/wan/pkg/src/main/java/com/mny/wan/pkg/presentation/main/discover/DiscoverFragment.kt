package com.mny.wan.pkg.presentation.main.discover

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.blankj.utilcode.util.LogUtils
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.mny.mojito.base.BaseFragment
import com.mny.wan.pkg.R
import com.mny.wan.pkg.presentation.adapter.CommonFragmentAdapter
import com.mny.wan.pkg.presentation.main.project.ProjectFragment
import com.mny.wan.pkg.presentation.main.system.SystemTagFragment
import com.mny.wan.pkg.presentation.main.system.share.ShareFragment
import com.mny.wan.pkg.presentation.main.wechat.WeChatFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DiscoverFragment : BaseFragment(R.layout.fragment_discover) {
    private var mTabLayout: TabLayout? = null
    private var mViewPage: ViewPager2? = null

    private val mFragments = mutableListOf<Fragment>()
    private lateinit var mVpAdapter: CommonFragmentAdapter

    override fun initView(view: View) {
        super.initView(view)
        mFragments.add(SystemTagFragment.newInstance(SystemTagFragment.TAG_SYSTEM))
        mFragments.add(SystemTagFragment.newInstance(SystemTagFragment.TAG_NAV))
        mFragments.add(ShareFragment.newInstance())
        mVpAdapter = CommonFragmentAdapter(this@DiscoverFragment, mFragments)
        LogUtils.d("=================================== ${mVpAdapter}")
        mTabLayout = view.findViewById(R.id.tab_layout)
        mViewPage = view.findViewById(R.id.view_pager)
        mTabLayout?.apply {
            tabMode = TabLayout.MODE_FIXED
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
            mViewPage?.adapter = mVpAdapter
            TabLayoutMediator(this, mViewPage!!) { tab, position ->
                tab.text = when (position) {
                    0 -> "体系"
                    3 -> "导航"
                    4 -> "广场"
                    else -> ""
                }
            }.attach()
        }
    }

    override fun onResume() {
        super.onResume()
        if (mViewPage?.adapter == null) {
            mViewPage?.adapter = mVpAdapter
            mVpAdapter.notifyDataSetChanged()
        }
    }

    override fun onPause() {
        super.onPause()
        mViewPage?.adapter = null
    }

    companion object {
        @JvmStatic
        fun newInstance() = DiscoverFragment()
    }
}