package com.mny.wan.pkg.presentation.main.system.secondsystem

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.blankj.utilcode.util.ActivityUtils
import com.google.android.material.tabs.TabLayout
import com.mny.wan.pkg.base.BaseBindingActivity
import com.mny.wan.pkg.data.remote.model.BeanSystemParent
import com.mny.wan.pkg.databinding.ActivitySystemChildrenBinding
import com.mny.wan.pkg.extension.initToolbar
import com.mny.wan.pkg.presentation.adapter.CommonFragmentViewPagerAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SystemChildrenActivity : BaseBindingActivity<ActivitySystemChildrenBinding>() {
    private var mSystemParent: BeanSystemParent? = null
    private val mFragments = mutableListOf<Fragment>()
    private val mTabs = mutableListOf<String>()
    private var mInitChildId = 0
    private lateinit var mVpAdapter: CommonFragmentViewPagerAdapter

    override fun initArgs(bundle: Bundle?, savedInstanceState: Bundle?): Boolean {
        mSystemParent = intent.getSerializableExtra(ARG_PARENT) as? BeanSystemParent
        mInitChildId = intent.getIntExtra(ARG_CHILD_ID, 0)
        return mSystemParent != null
    }

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        mSystemParent?.apply {
            initToolbar(mBinding.toolbar, this.name)
            mBinding.tabLayout.apply {
                tabMode = TabLayout.MODE_SCROLLABLE
                removeAllTabs()
                mFragments.clear()
                var selectedIndex = 0
                children.forEachIndexed { index, it ->
                    if (it.id == mInitChildId) {
                        selectedIndex = index
                    }
                    mFragments.add(SystemChildrenArticleFragment.newInstance(it.id))
                    mTabs.add(it.name)
                }
                mVpAdapter =
                    CommonFragmentViewPagerAdapter(supportFragmentManager, mFragments, mTabs)
                mBinding.viewPager.adapter = mVpAdapter
                mBinding.viewPager.currentItem = selectedIndex
                setupWithViewPager(mBinding.viewPager)
            }
        }
    }

    companion object {

        private const val ARG_PARENT = "TAG_PARENT"
        private const val ARG_CHILD_ID = "TAG_CHILD"

        fun show(parent: BeanSystemParent?, id: Int) {
            val intent = Bundle()
            intent.putSerializable(ARG_PARENT, parent)
            intent.putInt(ARG_CHILD_ID, id)
            ActivityUtils.startActivity(intent, SystemChildrenActivity::class.java)
        }
    }
}