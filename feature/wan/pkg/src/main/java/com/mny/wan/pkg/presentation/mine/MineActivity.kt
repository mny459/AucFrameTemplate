package com.mny.wan.pkg.presentation.mine

import android.os.Bundle
import com.blankj.utilcode.util.ActivityUtils
import com.mny.wan.base.BaseActivity
import com.mny.wan.pkg.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MineActivity : BaseActivity() {
    override fun layoutId(savedInstanceState: Bundle?): Int = R.layout.activity_mine

    companion object {
        fun show() {
            ActivityUtils.startActivity(MineActivity::class.java)
        }
    }
}