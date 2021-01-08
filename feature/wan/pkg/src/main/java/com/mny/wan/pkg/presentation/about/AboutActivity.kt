package com.mny.wan.pkg.presentation.about

import android.os.Bundle
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.AppUtils
import com.mny.wan.pkg.base.BaseBindingActivity
import com.mny.wan.pkg.databinding.ActivityAboutBinding
import com.mny.wan.pkg.extension.initToolbar

class AboutActivity : BaseBindingActivity<ActivityAboutBinding>() {
    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        initToolbar(mBinding.toolbar)
        mBinding.tvAppName.text = AppUtils.getAppName()
        mBinding.tvVerName.text = "${AppUtils.getAppVersionName()}-${AppUtils.getAppVersionCode()}"
    }

    companion object {
        fun show() {
            ActivityUtils.startActivity(AboutActivity::class.java)
        }
    }
}