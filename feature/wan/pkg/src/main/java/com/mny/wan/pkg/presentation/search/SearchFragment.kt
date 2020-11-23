package com.mny.wan.pkg.presentation.search

import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.LogUtils
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.mny.wan.base.BaseFragment
import com.mny.wan.pkg.R
import com.mny.wan.pkg.data.remote.model.BeanHotKey
import com.mny.wan.pkg.data.remote.model.BeanMultiType
import com.mny.wan.pkg.presentation.adapter.tag.TagAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : BaseFragment(R.layout.fragment_search) {
    private var mRvHotKey: RecyclerView? = null
    private val mViewModel: SearchViewModel by activityViewModels()
    override fun initView(view: View) {
        super.initView(view)
        mRvHotKey = view.findViewById(R.id.rvSearch)
        val layoutManager = FlexboxLayoutManager(mActivity)
        layoutManager.flexDirection = FlexDirection.ROW
        layoutManager.justifyContent = JustifyContent.FLEX_START
        mRvHotKey?.layoutManager = layoutManager

        mViewModel.mHotKey?.observe(this, object : Observer<MutableList<BeanHotKey>> {
            override fun onChanged(t: MutableList<BeanHotKey>?) {
                t?.apply {
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
                    mRvHotKey?.adapter = adapter
                }
            }
        })
    }

    override fun onFirstInit() {
        super.onFirstInit()
        mViewModel.fetchHotKey()
    }
}