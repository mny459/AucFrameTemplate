package com.mny.wan.pkg.presentation.search

import android.annotation.SuppressLint
import android.app.SearchManager
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.view.Menu
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.core.widget.addTextChangedListener
import androidx.navigation.findNavController
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.LogUtils
import com.mny.wan.pkg.R
import com.mny.wan.pkg.base.BaseBindingActivity
import com.mny.wan.pkg.base.BaseToolbarActivity
import com.mny.wan.pkg.databinding.ActivitySearchBinding
import com.mny.wan.pkg.extension.initToolbar
import dagger.hilt.android.AndroidEntryPoint
import org.jetbrains.anko.sdk27.coroutines.textChangedListener

@AndroidEntryPoint
class SearchActivity : BaseBindingActivity<ActivitySearchBinding>() {

    private val mViewModel: SearchViewModel by viewModels()

    override fun layoutId(savedInstanceState: Bundle?): Int = R.layout.activity_search

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        mBinding.imgBack.setOnClickListener { onBackPressed() }
        mBinding.etContent.addTextChangedListener {
            val text = it?.toString()?.trim() ?: ""
            search(text)
        }

        mBinding.imgSearch.setOnClickListener {
            val text = mBinding.etContent.text.toString().trim()
            search(text)
        }
    }

    override fun initObserver() {
        super.initObserver()
        mViewModel.observerSearchKeys()
    }

    fun search(query: String) {
        LogUtils.d("搜索 $query")
        if (query.isEmpty()) {
            findNavController(R.id.nav_host_fragment).popBackStack()
        } else {
            mViewModel.search(query)
            findNavController(R.id.nav_host_fragment).navigate(R.id.searchResultFragment)
        }
    }

    companion object {
        fun show() {
            ActivityUtils.startActivity(SearchActivity::class.java)
        }
    }
}