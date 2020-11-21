package com.mny.mojito.im.presentation.account

import android.os.Bundle
import com.mny.mojito.base.BaseActivity
import com.mny.mojito.im.R
import com.mny.mojito.im.extension.loadBg
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