package com.mny.wan.pkg.presentation.mine

import android.graphics.Paint
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.Group
import androidx.fragment.app.viewModels
import com.mny.wan.base.BaseFragment
import com.mny.wan.entension.observe
import com.mny.wan.pkg.R
import com.mny.wan.pkg.base.BaseBindingFragment
import com.mny.wan.pkg.databinding.FragmentMineBinding
import com.mny.wan.pkg.presentation.coin.CoinDetailActivity
import com.mny.wan.pkg.presentation.collect.CollectActivity
import com.mny.wan.pkg.presentation.main.qa.QAFragment
import com.mny.wan.pkg.presentation.setting.ThemeSettingsActivity
import com.mny.wan.pkg.utils.ThemeHelper
import com.mny.wan.pkg.widget.CommonRowView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MineFragment : BaseBindingFragment<FragmentMineBinding>() {

    private val mViewModel: MineViewModel by viewModels()

    override fun initView(view: View) {
        super.initView(view)

        mBinding?.imgLogout?.setOnClickListener {
            mViewModel.logout()
        }
        mBinding?.rowCollect?.setOnClickListener {
            CollectActivity.show()
        }
        mBinding?.rowTheme?.setOnCheckedChangeListener { _, checked ->
            if (checked) {
                ThemeHelper.setNightMode()
            } else {
                ThemeHelper.setLightMode()
            }
        }
        mBinding?.rowSettings?.setOnClickListener {
            ThemeSettingsActivity.show()
        }
        mBinding?.rowAbout?.setOnClickListener { }
        mBinding?.tvCredits?.setOnClickListener { CoinDetailActivity.show() }
    }

    override fun onFirstInit() {
        super.onFirstInit()
        mViewModel.loadData()
    }

    override fun initObserver() {
        super.initObserver()
        observe(mViewModel.stateLiveData) {
            it?.user?.apply {
                mBinding?.tvName?.text = "$username"
                mBinding?.groupUserInfo?.visibility = View.VISIBLE
            }
            it?.coin?.apply {
                mBinding?.tvCredits?.paint?.flags = Paint.UNDERLINE_TEXT_FLAG
                mBinding?.tvCredits?.paint?.isAntiAlias = true
                mBinding?.tvCredits?.text = "积分：$coinCount 排名：$rank"
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = MineFragment()
    }
}