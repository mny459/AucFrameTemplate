package com.mny.wan.im.presentation.account

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.blankj.utilcode.util.ToastUtils
import com.mny.wan.base.BaseFragment
import com.mny.wan.im.R
import com.mny.wan.im.extension.showLoading
import com.mny.wan.entension.observe
import com.mny.wan.im.presentation.main.MainActivity
import com.mny.wan.mvvm.BaseViewState
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.login_fragment.*
import org.jetbrains.anko.support.v4.startActivity

@AndroidEntryPoint
class LoginFragment : BaseFragment(R.layout.login_fragment) {

    companion object {
        fun newInstance() = LoginFragment()
    }

    private val mViewModel: LoginViewModel by viewModels()
    private var mLoadingDialog: QMUITipDialog? = null

    private val mViewStateObserver = Observer<BaseViewState> {
        when {
            it.loading -> {
                mLoadingDialog = mActivity?.showLoading("登录中")
            }
            it.complete -> {
                mLoadingDialog?.hide()
                if (it.errorMsg.isEmpty()) {
                    ToastUtils.showShort("登录成功")
                    startActivity<MainActivity>()
                } else {
//                    startActivity<MainActivity>()
                    ToastUtils.showShort("${it.errorMsg}")
                }
            }
        }
    }

    override fun initView(view: View) {
        observe(mViewModel.viewStateLiveData, mViewStateObserver)
        view.findViewById<Button>(R.id.btn_submit)?.setOnClickListener {
            mViewModel.login(et_phone.text.toString().trim(), et_password.text.toString().trim())
//            MessageActivity.go(mActivity!!,"213")
        }
        view.findViewById<TextView>(R.id.tv_go_register)?.setOnClickListener {
            findNavController().navigate(R.id.action_to_register)
        }
    }

    override fun initData(savedInstanceState: Bundle?) {

    }

}
