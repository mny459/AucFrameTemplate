package com.mny.wan.pkg.presentation.search

import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.navOptions
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.LogUtils
import com.mny.wan.pkg.R
import com.mny.wan.pkg.base.BaseBindingActivity
import com.mny.wan.pkg.databinding.ActivitySearchBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class SearchActivity : BaseBindingActivity<ActivitySearchBinding>() {

    private val mViewModel: SearchViewModel by viewModels()

    override fun layoutId(savedInstanceState: Bundle?): Int = R.layout.activity_search

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        mBinding.imgBack.setOnClickListener { onBackPressed() }
        mBinding.etContent.addTextChangedListener {
            val text = it?.toString()?.trimStart()?.trimEnd() ?: ""
            search(text)
        }

        mBinding.imgSearch.setOnClickListener {
            val text = mBinding.etContent.text.toString().trimStart().trimEnd()
            search(text)
        }
    }

    override fun initObserver() {
        super.initObserver()
        mViewModel.observerSearchKeys()
        lifecycleScope.launchWhenCreated {
            @OptIn(ExperimentalCoroutinesApi::class)
            mViewModel.searchKey.collectLatest {
                LogUtils.d("搜索内容是否为空 ${it.isEmpty()} $it")
                val navController = findNavController(R.id.nav_host_fragment)
                if (it.isEmpty()) {
                    navController.navigateUp()
                } else {
                    navController.navigate(
                        R.id.searchResultFragment,
                        null,
                        NavOptions.Builder().setLaunchSingleTop(true).build()
                    )
                }
            }
        }
    }

    private fun search(query: String) {
        mViewModel.search(query)
    }

    companion object {
        fun show() {
            ActivityUtils.startActivity(SearchActivity::class.java)
        }
    }
}