package com.mny.wan.im.presentation.account

import android.os.Bundle
import com.mny.wan.base.BaseActivity
import com.mny.wan.im.R
import com.mny.wan.im.extension.loadBg
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_account.*

@AndroidEntryPoint
class AccountActivity : BaseActivity() {
    override fun layoutId(savedInstanceState: Bundle?): Int = R.layout.activity_account
    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        loadBg(R.drawable.bg_src_tianjin, iv_bg)
    }
}