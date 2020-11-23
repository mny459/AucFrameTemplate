package com.mny.wan.pkg.presentation.webview

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.LogUtils
import com.mny.wan.base.BaseActivity
import com.mny.wan.entension.observe
import com.mny.wan.pkg.R
import com.mny.wan.pkg.base.BaseToolbarActivity
import dagger.hilt.android.AndroidEntryPoint

const val KEY_URL = "url"

@AndroidEntryPoint
class WebViewActivity : BaseToolbarActivity() {
    private val mViewModel: WebViewViewModel by viewModels()

    private var mUrl = ""

    override fun layoutId(savedInstanceState: Bundle?): Int = R.layout.activity_web_view

    override fun initArgs(bundle: Bundle?, savedInstanceState: Bundle?): Boolean {
        mUrl = bundle?.getString(KEY_URL) ?: mUrl
        return mUrl.isNotEmpty()
    }

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        mViewModel.initUrl(mUrl)
    }

    override fun initObserver() {
        super.initObserver()
        observe(mViewModel.stateLiveData, WebViewObserver())
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        mUrl = intent?.getStringExtra(KEY_URL) ?: mUrl
        LogUtils.d("mUrl = $mUrl")
    }

    inner class WebViewObserver : Observer<WebViewViewModel.ViewState> {
        override fun onChanged(t: WebViewViewModel.ViewState?) {
            t?.apply {
                mToolbar?.title = title
            }
        }
    }

    companion object {
        fun show(url: String) {
            val intent = Intent(ActivityUtils.getTopActivity(), WebViewActivity::class.java)
            intent.putExtra(KEY_URL, url)
            ActivityUtils.startActivity(intent)
        }
    }
}
