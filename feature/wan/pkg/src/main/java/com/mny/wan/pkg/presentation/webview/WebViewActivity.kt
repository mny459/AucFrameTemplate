package com.mny.wan.pkg.presentation.webview

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.LogUtils
import com.mny.mojito.base.BaseActivity
import com.mny.mojito.entension.observe
import com.mny.wan.pkg.R
import com.mny.wan.pkg.base.BaseBindingActivity
import com.mny.wan.pkg.base.BaseToolbarActivity
import com.mny.wan.pkg.databinding.ActivityWebViewBinding
import dagger.hilt.android.AndroidEntryPoint

const val KEY_URL = "url"

@AndroidEntryPoint
class WebViewActivity : BaseBindingActivity<ActivityWebViewBinding>() {
    private val mViewModel: WebViewViewModel by viewModels()

    private var mUrl = ""

    override fun layoutId(savedInstanceState: Bundle?): Int = R.layout.activity_web_view

    override fun initArgs(bundle: Bundle?, savedInstanceState: Bundle?): Boolean {
        mUrl = bundle?.getString(KEY_URL) ?: mUrl
        return mUrl.isNotEmpty()
    }

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        title = ""
        mViewModel.initUrl(mUrl)
    }

    override fun initObserver() {
        super.initObserver()
        observe(mViewModel.stateLiveData) {
            it?.apply {
                mBinding.toolbar.title = title
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        mUrl = intent?.getStringExtra(KEY_URL) ?: mUrl
        LogUtils.d("mUrl = $mUrl")
    }

    companion object {
        fun show(url: String) {
            val intent = Intent(ActivityUtils.getTopActivity(), WebViewActivity::class.java)
            intent.putExtra(KEY_URL, url)
            ActivityUtils.startActivity(intent)
        }
    }
}
