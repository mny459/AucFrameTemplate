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
import com.mny.wan.base.BaseFragment
import com.mny.wan.pkg.R
import com.mny.wan.pkg.data.remote.model.BeanMultiType
import com.mny.wan.pkg.data.remote.model.BeanNav
import com.mny.wan.pkg.data.remote.model.BeanSystemChildren
import com.mny.wan.pkg.data.remote.model.BeanSystemParent
import com.mny.wan.pkg.presentation.adapter.tag.TagAdapter
import com.mny.wan.pkg.presentation.main.system.secondsystem.SystemChildrenActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SystemTagFragment : BaseFragment(R.layout.fragment_system_tag) {
    private var mTag = TAG_SYSTEM
    private var mRvTags: RecyclerView? = null
    private val mViewModel: SystemTagViewModel by activityViewModels<SystemTagViewModel>()

    override fun initArgs(bundle: Bundle?) {
        super.initArgs(bundle)
        mTag = bundle?.getInt(TAG, TAG_SYSTEM) ?: TAG_SYSTEM
    }

    override fun initObserver() {
        super.initObserver()

        when (mTag) {
            TAG_SYSTEM -> {
                mViewModel.mSystemTree?.observe(
                    this,
                    object : Observer<MutableList<BeanSystemParent>> {
                        override fun onChanged(list: MutableList<BeanSystemParent>?) {
                            refreshSystemData(list)
                        }
                    })


            }
            TAG_NAV -> {
                mViewModel.mNavTree?.observe(
                    this,
                    object : Observer<MutableList<BeanNav>> {
                        override fun onChanged(list: MutableList<BeanNav>?) {
                            LogUtils.d("${list?.size}")
                            refreshNavData(list)
                        }
                    })
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

    fun refreshSystemData(list: MutableList<BeanSystemParent>?) {
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
                val intent = Intent(mActivity, SystemChildrenActivity::class.java)
                intent.putExtra(
                    "TAG_PARENT",
                    list?.firstOrNull { parent -> it.parentChapterId == parent.id })
                intent.putExtra("TAG_CHILD", id)
                startActivity(intent)
            }
        }
        mRvTags?.adapter = adapter
    }

    fun refreshNavData(list: MutableList<BeanNav>?) {
        val data = mutableListOf<BeanMultiType>()
        list?.forEach { parent ->
            data.add(BeanMultiType(parent, BeanMultiType.TYPE_PARENT))
            parent.articles.forEach { child ->
                data.add(BeanMultiType(child, BeanMultiType.TYPE_CHILD))
            }
        }
        val adapter = TagAdapter(data)
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