package com.mny.wan.pkg.presentation.mine

import android.graphics.Paint
import android.view.View
import androidx.fragment.app.viewModels
import com.mny.mojito.entension.gone
import com.mny.mojito.entension.observe
import com.mny.mojito.entension.visible
import com.mny.wan.pkg.base.BaseBindingFragment
import com.mny.wan.pkg.databinding.FragmentMineBinding
import com.mny.wan.pkg.presentation.AppViewModel
import com.mny.wan.pkg.presentation.coin.CoinDetailActivity
import com.mny.wan.pkg.presentation.collect.CollectActivity
import com.mny.wan.pkg.presentation.login.LoginActivity
import com.mny.wan.pkg.presentation.main.project.ProjectActivity
import com.mny.wan.pkg.presentation.main.system.share.ShareActivity
import com.mny.wan.pkg.presentation.main.wechat.WeChatActivity
import com.mny.wan.pkg.presentation.setting.SettingsActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MineFragment : BaseBindingFragment<FragmentMineBinding>() {

    private val mViewModel: MineViewModel by viewModels()

    @Inject
    lateinit var mAppViewModel: AppViewModel

    override fun initView(view: View) {
        super.initView(view)

        mBinding.imgLogout.setOnClickListener { mAppViewModel.logout() }
        mBinding.tvLogin.setOnClickListener { LoginActivity.show() }
        mBinding.rowCollect.setOnClickListener { CollectActivity.show() }
        mBinding.rowProject.setOnClickListener { ProjectActivity.show() }
        mBinding.rowWeChatArticle.setOnClickListener { WeChatActivity.show() }
        mBinding.rowSquare.setOnClickListener { ShareActivity.show() }
        mBinding.rowSettings.setOnClickListener { SettingsActivity.show() }
        mBinding.tvCredits.setOnClickListener { CoinDetailActivity.show() }
    }

    override fun onFirstInit() {
        super.onFirstInit()
        mViewModel.loadData()
    }

    override fun initObserver() {
        super.initObserver()
        observe(mAppViewModel.userInfo) {
            if (it == null) {
                mBinding.groupUserInfo.gone()
                mBinding.tvLogin.visible()
            } else {
                mBinding.groupUserInfo.visible()
                mBinding.tvLogin.gone()
                it.apply {
                    mBinding.tvName.text = "$username"
                }
            }
        }
        observe(mAppViewModel.coinInfo) {
            if (it == null) {
                mBinding.groupUserInfo.gone()
                mBinding.tvLogin.visible()
            } else {
                mBinding.groupUserInfo.visible()
                mBinding.tvLogin.gone()
                it.apply {
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