package com.mny.wan.pkg.presentation.main.wechat

import android.text.Html
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.mny.wan.base.BaseFragment
import com.mny.wan.entension.observe
import com.mny.wan.pkg.R
import com.mny.wan.pkg.data.remote.model.BeanSystemParent
import com.mny.wan.pkg.presentation.adapter.CommonFragmentAdapter
import com.mny.wan.pkg.presentation.main.wechat.article.WeChatArticleFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WeChatFragment : BaseFragment(R.layout.fragment_we_chat) {
    private var mTabLayout: TabLayout? = null
    private var mViewPage: ViewPager2? = null
    private var mToolbar: MaterialToolbar? = null

    private val mFragments = mutableListOf<Fragment>()
    private lateinit var mVpAdapter: CommonFragmentAdapter
    private val mViewModel: WeChatViewModel by viewModels()
    override fun initObserver() {
        super.initObserver()
        observe(mViewModel.mTabs, ProjectObserver())
    }

    override fun initView(view: View) {
        super.initView(view)
        mTabLayout = view.findViewById(R.id.tab_layout)
        mViewPage = view.findViewById(R.id.view_pager)
        mToolbar = view.findViewById(R.id.toolbar)
        mToolbar?.title = "公众号文章"
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
    }

    inner class ProjectObserver : Observer<MutableList<BeanSystemParent>> {
        override fun onChanged(tabs: MutableList<BeanSystemParent>) {
            mTabLayout?.apply {
                removeAllTabs()
                mFragments.clear()

                tabs.forEach { projectType ->
                    mFragments.add(WeChatArticleFragment.newInstance(projectType.id))
                }

                mVpAdapter = CommonFragmentAdapter(this@WeChatFragment, mFragments)
                mViewPage?.adapter = mVpAdapter
                TabLayoutMediator(this, mViewPage!!) { tab, position ->
                    tab.text = "${Html.fromHtml(tabs[position].name)}"
                }.attach()
            }
        }

    }


    override fun onFirstInit() {
        super.onFirstInit()
        mViewModel.loadData()
    }
}