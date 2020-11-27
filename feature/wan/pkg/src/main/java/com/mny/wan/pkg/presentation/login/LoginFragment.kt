package com.mny.wan.pkg.presentation.login

import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.blankj.utilcode.util.ToastUtils
import com.mny.wan.base.BaseFragment
import com.mny.wan.entension.observe
import com.mny.wan.pkg.R
import com.mny.wan.pkg.extension.showLoading
import com.mny.wan.pkg.presentation.main.MainActivity
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : BaseFragment(R.layout.login_fragment) {

    private val mViewModel: LoginViewModel by viewModels()
    private var mLoadingDialog: QMUITipDialog? = null

    private val mStateObserver = Observer<LoginViewModel.ViewState> {
        when {
            it.isLoading -> {
                mLoadingDialog = mActivity?.showLoading("登录中")
            }
            it.loginSuccess -> {
                mLoadingDialog?.hide()
                lifecycleScope.launch {
                    ToastUtils.showShort("登录成功")
                    delay(1000)
                    MainActivity.show()
                    mActivity?.finish()
                }

            }
            it.isError -> {
                mLoadingDialog?.hide()
                ToastUtils.showShort("${it.errorMsg}")
            }
        }
    }

    override fun initView(view: View) {
        view.findViewById<Button>(R.id.btnLogin)?.setOnClickListener {
            // TODO 省略参数检查
            mViewModel.login(
                view.findViewById<EditText>(R.id.etName).text.toString().trim(),
                view.findViewById<EditText>(R.id.etPassword).text.toString().trim()
            )
        }
        view.findViewById<TextView>(R.id.btnRegister)?.setOnClickListener {
            findNavController().navigate(R.id.action_to_register)
        }
    }

    override fun initObserver() {
        super.initObserver()
        observe(mViewModel.stateLiveData, mStateObserver)
    }
}
