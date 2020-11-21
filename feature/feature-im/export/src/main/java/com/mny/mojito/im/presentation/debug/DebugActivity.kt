package com.mny.mojito.im.presentation.debug

import android.os.Bundle
import androidx.activity.viewModels
import com.google.android.material.button.MaterialButton
import com.mny.mojito.im.R
import com.mny.mojito.im.base.BaseToolbarActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DebugActivity : BaseToolbarActivity() {

    private var mBtnCreateUser: MaterialButton? = null
    private var mBtnCreateContact: MaterialButton? = null
    private var mBtnCreateGroup: MaterialButton? = null
    private var mBtnShowSendBtn: MaterialButton? = null

    private val mViewModel: DebugViewModel by viewModels<DebugViewModel>()
    override fun layoutId(savedInstanceState: Bundle?): Int = R.layout.activity_debug

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        mBtnCreateUser = findViewById(R.id.btn_create_user)
        mBtnCreateContact = findViewById(R.id.btn_create_contact)
        mBtnCreateGroup = findViewById(R.id.btn_create_group)
        mBtnShowSendBtn = findViewById(R.id.btn_show_send_btn)
        mBtnCreateUser?.setOnClickListener { mViewModel.createUser() }
        mBtnCreateContact?.setOnClickListener { mViewModel.createContact() }
        mBtnCreateGroup?.setOnClickListener { mViewModel.createGroup() }
        mBtnShowSendBtn?.setOnClickListener { mViewModel.showSendBtn() }


    }

}