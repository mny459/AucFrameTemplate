package com.mny.wan.pkg.presentation.coin

import android.os.Bundle
import com.blankj.utilcode.util.ActivityUtils
import com.mny.mojito.base.BaseActivity
import com.mny.wan.pkg.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CoinDetailActivity : BaseActivity() {
    override fun layoutId(savedInstanceState: Bundle?): Int = R.layout.activity_coin_detail

    companion object {
        fun show() {
            ActivityUtils.startActivity(CoinDetailActivity::class.java)
        }
    }
}