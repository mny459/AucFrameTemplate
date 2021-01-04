package com.mny.wan.pkg.presentation.login

import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.blankj.utilcode.util.ToastUtils
import com.mny.mojito.entension.observe
import com.mny.wan.pkg.R
import com.mny.wan.pkg.base.BaseBindingFragment
import com.mny.wan.pkg.databinding.LoginFragmentBinding
import com.mny.wan.pkg.presentation.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : BaseBindingFragment<LoginFragmentBinding>() {

    private val mViewModel: LoginViewModel by viewModels()

    override fun initView(view: View) {
        mBinding.toolbar.setNavigationOnClickListener {
            mActivity?.onBackPressed()
        }
        mBinding.btnLogin.setOnClickListener {
            // TODO 省略参数检查
            mViewModel.login(
                mBinding.etName.text.toString().trim(),
                mBinding.etPassword.text.toString().trim()
            )
        }
        mBinding.btnRegister.setOnClickListener {
            findNavController().navigate(R.id.action_to_register)
        }
    }

    override fun initObserver() {
        super.initObserver()
        observe(mViewModel.stateLiveData) {
            when {
                it.isLoading -> {
//                mLoadingDialog =
//                    mActivity?.showLoading(StringUtils.getString(R.string.login_loading))
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
}
