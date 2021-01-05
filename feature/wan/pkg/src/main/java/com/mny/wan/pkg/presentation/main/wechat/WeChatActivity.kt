package com.mny.wan.pkg.presentation.main.wechat

import com.blankj.utilcode.util.ActivityUtils
import com.mny.wan.pkg.base.BaseBindingActivity
import com.mny.wan.pkg.databinding.ActivityWeChatBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WeChatActivity : BaseBindingActivity<ActivityWeChatBinding>() {

    companion object {
        fun show() {
            ActivityUtils.startActivity(WeChatActivity::class.java)
        }
    }
}