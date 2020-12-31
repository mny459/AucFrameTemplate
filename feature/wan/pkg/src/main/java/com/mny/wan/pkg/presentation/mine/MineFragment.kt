package com.mny.wan.pkg.presentation.mine

import android.graphics.Paint
import android.view.View
import androidx.fragment.app.viewModels
import com.mny.mojito.entension.gone
import com.mny.mojito.entension.observe
import com.mny.mojito.entension.visible
import com.mny.wan.pkg.base.BaseBindingFragment
import com.mny.wan.pkg.databinding.FragmentMineBinding
import com.mny.wan.pkg.presentation.coin.CoinDetailActivity
import com.mny.wan.pkg.presentation.collect.CollectActivity
import com.mny.wan.pkg.presentation.login.LoginActivity
import com.mny.wan.pkg.presentation.setting.ThemeSettingsActivity
import com.mny.wan.pkg.utils.ThemeHelper
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MineFragment : BaseBindingFragment<FragmentMineBinding>() {

    private val mViewModel: MineViewModel by viewModels()

    override fun initView(view: View) {
        super.initView(view)

        mBinding.imgLogout.setOnClickListener {
            mViewModel.logout()
        }
        mBinding.tvLogin.setOnClickListener {
            LoginActivity.go()
        }
        mBinding.rowCollect.setOnClickListener {
            CollectActivity.show()
        }
        mBinding.rowTheme.setOnCheckedChangeListener { _, checked ->
            if (checked) {
                ThemeHelper.setNightMode()
            } else {
                ThemeHelper.setLightMode()
            }
        }
        mBinding.rowSettings.setOnClickListener {
            ThemeSettingsActivity.show()
        }
        mBinding.rowAbout.setOnClickListener { }
        mBinding.tvCredits.setOnClickListener { CoinDetailActivity.show() }
    }

    override fun onFirstInit() {
        super.onFirstInit()
        mViewModel.loadData()
    }

    override fun initObserver() {
        super.initObserver()
        observe(mViewModel.stateLiveData) {
            if (it.user == null || it.coin == null) {
                mBinding.groupUserInfo.gone()
                mBinding.tvLogin.visible()
            } else {
                mBinding.groupUserInfo.visible()
                mBinding.tvLogin.gone()
                it?.user?.apply {
                    mBinding.tvName.text = "$username"
                }
                it?.coin?.apply {
                    mBinding.tvCredits.paint.flags = Paint.UNDERLINE_TEXT_FLAG
                    mBinding.tvCredits.paint.isAntiAlias = true
                    mBinding.tvCredits.text = "积分：$coinCount 排名：$rank"
                }
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = MineFragment()
    }
}