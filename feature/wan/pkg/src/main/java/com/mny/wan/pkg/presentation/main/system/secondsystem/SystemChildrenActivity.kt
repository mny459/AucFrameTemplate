package com.mny.wan.pkg.presentation.main.system.secondsystem

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.blankj.utilcode.util.LogUtils
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.mny.mojito.base.BaseActivity
import com.mny.wan.pkg.R
import com.mny.wan.pkg.data.remote.model.BeanSystemParent
import com.mny.wan.pkg.presentation.adapter.CommonFragmentAdapterForActivity
import dagger.hilt.android.AndroidEntryPoint
import org.jetbrains.anko.collections.forEachWithIndex

@AndroidEntryPoint
class SystemChildrenActivity : BaseActivity() {
    private var mSystemParent: BeanSystemParent? = null
    private var mTabLayout: TabLayout? = null
    private var mViewPage: ViewPager2? = null

    private val mFragments = mutableListOf<Fragment>()
    private lateinit var mVpAdapter: CommonFragmentAdapterForActivity

    override fun layoutId(savedInstanceState: Bundle?): Int = R.layout.activity_system_children

    override fun initArgs(bundle: Bundle?, savedInstanceState: Bundle?): Boolean {
        mSystemParent = intent.getSerializableExtra("TAG_PARENT") as? BeanSystemParent
        return mSystemParent != null
    }

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        mTabLayout = findViewById(R.id.tab_layout)
        mViewPage = findViewById(R.id.view_pager)
        mTabLayout?.apply {
            mTabLayout?.tabMode = TabLayout.MODE_SCROLLABLE
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
        }
        mSystemParent?.apply {
            mTabLayout?.apply {
                removeAllTabs()
                mFragments.clear()
                var selectedIndex = 0
                children.forEachWithIndex { index, it ->
                    LogUtils.d("${intent.getIntExtra("TAG_CHILD", 0)},${it.id}")
                    if (it.id == intent.getIntExtra("TAG_CHILD", 0)) {
                        selectedIndex = index
                    }
                    mFragments.add(SystemChildrenArticleFragment.newInstance(it.id))
                }
                mVpAdapter =
                    CommonFragmentAdapterForActivity(this@SystemChildrenActivity, mFragments)
                mViewPage?.adapter = mVpAdapter
                TabLayoutMediator(this, mViewPage!!) { tab, position ->
                    tab.text = "${children[position].name}"
                }.attach()
                mViewPage?.currentItem = selectedIndex
            }
        }
    }

    fun show(context: BaseActivity, parent: BeanSystemParent) {


    }
}