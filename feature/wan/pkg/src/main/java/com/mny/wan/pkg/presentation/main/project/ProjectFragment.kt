package com.mny.wan.pkg.presentation.main.project

import android.text.Html
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.mny.mojito.base.BaseFragment
import com.mny.mojito.entension.observe
import com.mny.wan.pkg.R
import com.mny.wan.pkg.data.remote.model.BeanProject
import com.mny.wan.pkg.presentation.adapter.CommonFragmentAdapter
import com.mny.wan.pkg.presentation.main.project.article.ProjectArticleFragment
import com.mny.wan.pkg.presentation.main.wechat.WeChatFragment
import dagger.hilt.android.AndroidEntryPoint
import java.lang.ref.WeakReference
import java.util.*

@AndroidEntryPoint
class ProjectFragment : BaseFragment(R.layout.fragment_project) {
    private var mTabLayout: TabLayout? = null
    private var mViewPage: ViewPager2? = null

    private val mFragments = mutableListOf<Fragment>()
    private lateinit var mVpAdapter: CommonFragmentAdapter
    private val mViewModel: ProjectViewModel by activityViewModels()
    override fun initObserver() {
        super.initObserver()
        observe(mViewModel.mTabs, ProjectObserver())
    }

    override fun initView(view: View) {
        super.initView(view)
        mTabLayout = view.findViewById(R.id.tab_layout)
        mViewPage = view.findViewById(R.id.view_pager)
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

    inner class ProjectObserver : Observer<MutableList<BeanProject>> {
        override fun onChanged(tabs: MutableList<BeanProject>) {
            mTabLayout?.apply {
                removeAllTabs()
                mFragments.clear()

                tabs.forEach { projectType ->
                    mFragments.add(ProjectArticleFragment.newInstance(projectType.id))
                }

                mVpAdapter = CommonFragmentAdapter(this@ProjectFragment, mFragments)
                mViewPage?.adapter = mVpAdapter
                mViewPage?.offscreenPageLimit = ViewPager2.OFFSCREEN_PAGE_LIMIT_DEFAULT
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

    companion object {
        @JvmStatic
        fun newInstance() = ProjectFragment()
    }

}