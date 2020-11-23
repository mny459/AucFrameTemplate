package com.mny.wan.pkg.presentation.webview

import android.content.Intent
import android.os.Bundle
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.LogUtils
import com.mny.wan.base.BaseActivity
import com.mny.wan.pkg.R

const val KEY_URL = "url"

class WebViewActivity : BaseActivity() {
    private var mUrl = ""

    override fun layoutId(savedInstanceState: Bundle?): Int = R.layout.activity_web_view

    override fun initArgs(bundle: Bundle?, savedInstanceState: Bundle?): Boolean {
        mUrl = bundle?.getString(KEY_URL) ?: mUrl
        return mUrl.isNotEmpty()
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
