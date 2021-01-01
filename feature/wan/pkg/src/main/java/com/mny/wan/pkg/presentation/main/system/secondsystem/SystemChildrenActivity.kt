package com.mny.wan.pkg.presentation.main.system.secondsystem

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import com.mny.mojito.base.BaseActivity
import com.mny.wan.pkg.base.BaseBindingActivity
import com.mny.wan.pkg.data.remote.model.BeanSystemParent
import com.mny.wan.pkg.databinding.ActivitySystemChildrenBinding
import com.mny.wan.pkg.extension.initToolbar
import com.mny.wan.pkg.presentation.adapter.CommonFragmentViewPagerAdapter
import dagger.hilt.android.AndroidEntryPoint
import org.jetbrains.anko.collections.forEachWithIndex

@AndroidEntryPoint
class SystemChildrenActivity : BaseBindingActivity<ActivitySystemChildrenBinding>() {
    private var mSystemParent: BeanSystemParent? = null
    private val mFragments = mutableListOf<Fragment>()
    private val mTabs = mutableListOf<String>()
    private lateinit var mVpAdapter: CommonFragmentViewPagerAdapter

    override fun initArgs(bundle: Bundle?, savedInstanceState: Bundle?): Boolean {
        mSystemParent = intent.getSerializableExtra("TAG_PARENT") as? BeanSystemParent
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
                children.forEachWithIndex { index, it ->
                    if (it.id == intent.getIntExtra("TAG_CHILD", 0)) {
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

    fun show(context: BaseActivity, parent: BeanSystemParent) {
//        val intent = Intent(mActivity, SystemChildrenActivity::class.java)
//        intent.putExtra(
//            "TAG_PARENT",
//            list?.firstOrNull { parent -> it.parentChapterId == parent.id })
//        intent.putExtra("TAG_CHILD", id)
//        startActivity(intent)
    }
}