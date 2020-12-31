package com.mny.wan.pkg.presentation.main.system

import androidx.fragment.app.Fragment
import android.view.View
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.mny.mojito.base.BaseFragment
import com.mny.wan.pkg.R
import com.mny.wan.pkg.presentation.adapter.CommonFragmentAdapter
import com.mny.wan.pkg.presentation.main.system.share.ShareFragment
import com.mny.wan.pkg.presentation.main.wechat.WeChatFragment
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
        mVpAdapter = CommonFragmentAdapter(this@SystemFragment, mFragments)

        mTabLayout = view.findViewById(R.id.tab_layout)
        mViewPage = view.findViewById(R.id.view_pager)
        mTabLayout?.apply {
            tabMode = TabLayout.MODE_FIXED
            mViewPage?.adapter = mVpAdapter
            TabLayoutMediator(this, mViewPage!!) { tab, position ->
                tab.text = when (position) {
                    0 -> "体系"
                    1 -> "导航"
                    else -> ""
                }
            }.attach()
        }

    }

    companion object {
        @JvmStatic
        fun newInstance() = SystemFragment()
    }
}