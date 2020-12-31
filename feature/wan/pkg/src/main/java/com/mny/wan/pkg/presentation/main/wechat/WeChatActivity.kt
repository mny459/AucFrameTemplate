package com.mny.wan.pkg.presentation.main.wechat

import android.os.Bundle
import com.blankj.utilcode.util.ActivityUtils
import com.mny.mojito.base.BaseActivity
import com.mny.wan.pkg.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WeChatActivity : BaseActivity() {

    override fun layoutId(savedInstanceState: Bundle?): Int = R.layout.activity_we_chat

    companion object {
        fun show() {
            ActivityUtils.startActivity(WeChatActivity::class.java)
        }
    }
}