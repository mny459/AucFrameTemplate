package com.mny.wan.pkg.presentation.main.system

import androidx.fragment.app.Fragment
import android.view.View
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.mny.mojito.base.BaseFragment
import com.mny.wan.pkg.R
import com.mny.wan.pkg.base.BaseBindingFragment
import com.mny.wan.pkg.databinding.FragmentSystemBinding
import com.mny.wan.pkg.presentation.adapter.CommonFragmentAdapter
import com.mny.wan.pkg.presentation.adapter.CommonFragmentViewPagerAdapter
import com.mny.wan.pkg.presentation.main.system.share.ShareFragment
import com.mny.wan.pkg.presentation.main.wechat.WeChatFragment
import com.mny.wan.pkg.presentation.main.wechat.article.WeChatArticleFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SystemFragment : BaseBindingFragment<FragmentSystemBinding>() {

    private val mFragments = listOf<Fragment>(
        SystemTagFragment.newInstance(SystemTagFragment.TAG_SYSTEM),
        SystemTagFragment.newInstance(SystemTagFragment.TAG_NAV)
    )

    private val mTabs = listOf("体系", "导航")

    private lateinit var mVpAdapter: CommonFragmentViewPagerAdapter

    override fun initView(view: View) {
        super.initView(view)
        mVpAdapter = CommonFragmentViewPagerAdapter(this.childFragmentManager, mFragments.toMutableList(), mTabs)
        mBinding.tabLayout.apply {
            tabMode = TabLayout.MODE_FIXED
            removeAllTabs()
            mBinding.viewPager.adapter = mVpAdapter
            setupWithViewPager(mBinding.viewPager)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = SystemFragment()
    }
}