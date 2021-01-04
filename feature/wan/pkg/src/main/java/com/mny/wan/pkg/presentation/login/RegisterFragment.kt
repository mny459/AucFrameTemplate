package com.mny.wan.pkg.presentation.login

import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.blankj.utilcode.util.ToastUtils
import com.mny.mojito.base.BaseFragment
import com.mny.mojito.entension.observe
import com.mny.wan.pkg.R
import com.mny.wan.pkg.base.BaseBindingFragment
import com.mny.wan.pkg.databinding.RegisterFragmentBinding
import com.mny.wan.pkg.presentation.main.MainActivity
import com.mny.wan.pkg.presentation.mine.MineViewModel

import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegisterFragment : BaseBindingFragment<RegisterFragmentBinding>() {
    private val mViewModel: LoginViewModel by viewModels()

    companion object {
        fun newInstance() = RegisterFragment()
    }

    override fun initObserver() {
        super.initObserver()
        mBinding.toolbar.setNavigationOnClickListener {
            mActivity?.onBackPressed()
        }
        observe(mViewModel.stateLiveData){
            when {
                it.isLoading -> {
                }
                it.loginSuccess -> {
                    lifecycleScope.launch {
                        ToastUtils.showShort(R.string.login_success)
                        delay(1000)
                        MainActivity.show()
                        mActivity?.finish()
                    }

                }
                it.isError -> {
                    ToastUtils.showShort("${it.errorMsg}")
                }
            }
        }
    }

    override fun initView(view: View) {
        mBinding.btnRegister.setOnClickListener {
            // TODO 省略参数检查
            mViewModel.register(
                mBinding.etName.text.toString().trim(),
                mBinding.etPassword.text.toString().trim(),
                mBinding.etRePassword.text.toString().trim()
            )
        }
    }
}
