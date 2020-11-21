package com.mny.mojito.im.presentation.account

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.blankj.utilcode.util.ToastUtils
import com.mny.mojito.base.BaseFragment

import com.mny.mojito.im.R
import com.mny.mojito.im.extension.showLoading
import com.mny.mojito.entension.observe
import com.mny.mojito.mvvm.BaseViewState
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.register_fragment.*

@AndroidEntryPoint
class RegisterFragment : BaseFragment(R.layout.register_fragment) {
    private val mViewModel: RegisterViewModel by viewModels()
    private var mLoadingDialog: QMUITipDialog? = null

    protected val mViewStateObserver = Observer<BaseViewState> {
        when {
            it.loading -> {
                mLoadingDialog = mActivity?.showLoading("注册中")
            }
            it.complete -> {
                mLoadingDialog?.hide()
                if (it.errorMsg.isNotEmpty()) {
                    ToastUtils.showShort("注册成功")
                } else {
                    ToastUtils.showShort("${it.errorMsg}")
                }
            }
        }
    }

    companion object {
        fun newInstance() = RegisterFragment()
    }

    override fun initView(view: View) {
        observe(mViewModel.viewStateLiveData, mViewStateObserver)
        view.findViewById<Button>(R.id.btn_submit)?.setOnClickListener {
            mViewModel.register(et_phone.text.toString().trim(), et_name.text.toString().trim(),
                    et_password.text.toString().trim())
        }
        tv_go_login?.setOnClickListener {
            findNavController().navigate(R.id.action_to_register)
        }
    }

    override fun initData(savedInstanceState: Bundle?) {

    }

}
