package com.mny.wan.pkg.presentation.main.project

import android.os.Bundle
import com.blankj.utilcode.util.ActivityUtils
import com.mny.mojito.base.BaseActivity
import com.mny.wan.pkg.R
import com.mny.wan.pkg.base.BaseBindingActivity
import com.mny.wan.pkg.databinding.ActivityProjectBinding
import com.mny.wan.pkg.extension.initToolbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProjectActivity : BaseBindingActivity<ActivityProjectBinding>() {

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        initToolbar(        mBinding.toolbar)
    }

    companion object {
        fun show() {
            ActivityUtils.startActivity(ProjectActivity::class.java)
        }
    }
}