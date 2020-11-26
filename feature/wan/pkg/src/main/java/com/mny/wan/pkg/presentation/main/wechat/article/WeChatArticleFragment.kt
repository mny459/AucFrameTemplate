package com.mny.wan.pkg.presentation.main.wechat.article

import android.os.Bundle
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.mny.wan.pkg.R
import com.mny.wan.pkg.base.BaseArticleFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class WeChatArticleFragment : BaseArticleFragment(R.layout.fragment_we_chat_article) {

    private var mWeChatId: Int = 0
    private val mViewModel: WeChatArticleViewModel by activityViewModels()
    override fun initArticleObserver() {
        lifecycleScope.launchWhenCreated {
            @OptIn(ExperimentalCoroutinesApi::class)
            mViewModel.mArticleList.collectLatest {
                mAdapter.submitData(it)
            }
        }
    }

    override fun initArgs(bundle: Bundle?) {
        super.initArgs(bundle)
        bundle?.let {
            mWeChatId = it.getInt(ARG_WE_CHAT_ID, 0)
        }
    }

    override fun initData(savedInstanceState: Bundle?) {
        super.initData(savedInstanceState)
        mViewModel.initCId(mWeChatId)
        mViewModel.loadData()
    }

    companion object {
        private const val ARG_WE_CHAT_ID = "we_chat_id"

        @JvmStatic
        fun newInstance(weChatId: Int) =
            WeChatArticleFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_WE_CHAT_ID, weChatId)
                }
            }
    }
}