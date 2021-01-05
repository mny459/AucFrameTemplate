package com.mny.wan.pkg.presentation.main.system.share

import android.os.Bundle
import com.blankj.utilcode.util.ActivityUtils
import com.mny.mojito.base.BaseActivity
import com.mny.wan.pkg.R
import com.mny.wan.pkg.base.BaseBindingActivity
import com.mny.wan.pkg.databinding.ActivityShareBinding
import com.mny.wan.pkg.extension.initToolbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ShareActivity : BaseBindingActivity<ActivityShareBinding>() {
    
    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        initToolbar(mBinding.toolbar)
    }

    companion object {
        fun show() {
            ActivityUtils.startActivity(ShareActivity::class.java)
        }
    }
}