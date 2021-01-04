package com.mny.wan.pkg.presentation.search

import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.LogUtils
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.mny.mojito.base.BaseFragment
import com.mny.wan.pkg.R
import com.mny.wan.pkg.base.BaseBindingFragment
import com.mny.wan.pkg.data.remote.model.BeanHotKey
import com.mny.wan.pkg.data.remote.model.BeanMultiType
import com.mny.wan.pkg.databinding.FragmentSearchBinding
import com.mny.wan.pkg.presentation.adapter.tag.TagAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : BaseBindingFragment<FragmentSearchBinding>() {

    private val mViewModel: SearchViewModel by activityViewModels()
    override fun initView(view: View) {
        super.initView(view)
        val layoutManager = FlexboxLayoutManager(mActivity)
        layoutManager.flexDirection = FlexDirection.ROW
        layoutManager.justifyContent = JustifyContent.FLEX_START
        mBinding?.rvSearch?.layoutManager = layoutManager

        mViewModel.mHotKey.observe(this) { hotKeys ->
            hotKeys?.apply {
                val tags = mutableListOf<BeanMultiType>()
                tags.add(BeanMultiType("热门搜索", BeanMultiType.TYPE_PARENT))
                this.forEach {
                    tags.add(BeanMultiType(it, BeanMultiType.TYPE_CHILD))
                }
                val adapter = TagAdapter(tags) {
                    if (it is BeanHotKey) {
                        LogUtils.d("$it")
                        mViewModel.search(it.name)
                    }
                }
                mBinding?.rvSearch?.adapter = adapter
            }
        }
    }

    override fun onFirstInit() {
        super.onFirstInit()
        mViewModel.fetchHotKey()
    }
}