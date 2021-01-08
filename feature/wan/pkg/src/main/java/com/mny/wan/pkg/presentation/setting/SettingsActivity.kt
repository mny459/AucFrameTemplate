package com.mny.wan.pkg.presentation.setting

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.LogUtils
import com.mny.mojito.entension.observe
import com.mny.wan.pkg.base.BaseBindingActivity
import com.mny.wan.pkg.databinding.ActivitySettingsBinding
import com.mny.wan.pkg.extension.initToolbar
import com.mny.wan.pkg.presentation.about.AboutActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SettingsActivity : BaseBindingActivity<ActivitySettingsBinding>() {
    private val mViewModel: SettingsViewModel by viewModels()

    override fun initObserver() {
        super.initObserver()
        ViewModelProvider.AndroidViewModelFactory
        observe(mViewModel.stateLiveData) { state ->
            mBinding.rowThemeSwitch.isEnabled = !state.themeFollowSystem
            LogUtils.d("更新了状态 $state")
            mBinding.rowThemeSwitch.setChecked(state.themeDark, false)
            mBinding.rowThemeFollowSystem.setChecked(state.themeFollowSystem, true)
            mBinding.rowLanguageSwitch.setChecked(!state.languageZH, false)
        }
    }

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        initToolbar(mBinding.toolbar)
        mBinding.rowThemeFollowSystem.setOnClickListener {
            val restart = mViewModel.switchThemeFollowSystem()
        }
        mBinding.rowThemeSwitch.setOnClickListener {
            val restart = mViewModel.switchThemeDark()
        }
        mBinding.rowLanguageSwitch.setOnClickListener {
            mViewModel.switchLanguage()
        }
        mBinding.rowAbout.setOnClickListener {
            AboutActivity.show()
        }
    }

    fun updateTheme() {
        lifecycleScope.launch {
            delay(300)
//            mViewModel.restartApp()
        }
    }

    companion object {
        fun show() {
            ActivityUtils.startActivity(SettingsActivity::class.java)
        }
    }
}