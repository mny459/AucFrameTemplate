package com.mny.wan.pkg.presentation.main.system

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.LogUtils
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.mny.mojito.base.BaseFragment
import com.mny.wan.pkg.R
import com.mny.wan.pkg.data.remote.model.*
import com.mny.wan.pkg.presentation.adapter.tag.TagAdapter
import com.mny.wan.pkg.presentation.main.system.secondsystem.SystemChildrenActivity
import com.mny.wan.pkg.presentation.webview.WebViewActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SystemTagFragment : BaseFragment(R.layout.fragment_system_tag) {

    private var mTag = TAG_SYSTEM
    private var mRvTags: RecyclerView? = null
    private val mViewModel: SystemTagViewModel by viewModels()

    override fun initArgs(bundle: Bundle?) {
        super.initArgs(bundle)
        mTag = bundle?.getInt(TAG, TAG_SYSTEM) ?: TAG_SYSTEM
    }

    override fun initObserver() {
        super.initObserver()
        when (mTag) {
            TAG_SYSTEM -> {
                mViewModel.mSystemTree.observe(this) { list ->
                    refreshSystemData(list)
                }
            }
            TAG_NAV -> {
                mViewModel.mNavTree.observe(this) { list ->
                    refreshNavData(list)
                }
            }
            else -> {
            }
        }

    }

    override fun initView(view: View) {
        super.initView(view)
        mViewModel.initTag(mTag)

        mRvTags = view.findViewById(R.id.rv_tags)
        val layoutManager = FlexboxLayoutManager(mActivity)
        layoutManager.flexDirection = FlexDirection.ROW
        layoutManager.justifyContent = JustifyContent.FLEX_START
        mRvTags?.layoutManager = layoutManager
    }

    override fun onFirstInit() {
        super.onFirstInit()
        mViewModel.loadData()
    }

    private fun refreshSystemData(list: MutableList<BeanSystemParent>?) {
        val data = mutableListOf<BeanMultiType>()
        list?.forEach { parent ->
            data.add(BeanMultiType(parent, BeanMultiType.TYPE_PARENT))
            parent.children.forEach { child ->
                //                LogUtils.d("$child")
                data.add(BeanMultiType(child, BeanMultiType.TYPE_CHILD))
            }
        }
        val adapter = TagAdapter(data) {
            if (it is BeanSystemChildren) {
                SystemChildrenActivity.show(
                    list?.firstOrNull { parent -> it.parentChapterId == parent.id },
                    it.id
                )
            }
        }
        mRvTags?.adapter = adapter
    }

    private fun refreshNavData(list: MutableList<BeanNav>?) {
        val data = mutableListOf<BeanMultiType>()
        list?.forEach { parent ->
            data.add(BeanMultiType(parent, BeanMultiType.TYPE_PARENT))
            parent.articles.forEach { child ->
                data.add(BeanMultiType(child, BeanMultiType.TYPE_CHILD))
            }
        }
        val adapter = TagAdapter(data) {
            if (it is BeanArticle) {
                WebViewActivity.show(it.link)
            }
        }
        mRvTags?.adapter = adapter
    }

    companion object {
        const val TAG = "tag"
        const val TAG_SYSTEM = 0
        const val TAG_NAV = 1
        fun newInstance(tag: Int): SystemTagFragment = SystemTagFragment()
            .apply {
                arguments = Bundle().apply {
                    putInt(TAG, tag)
                }
            }
    }
}