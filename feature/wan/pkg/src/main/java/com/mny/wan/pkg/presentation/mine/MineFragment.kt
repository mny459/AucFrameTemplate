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
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MineFragment : BaseFragment(R.layout.fragment_mine) {
    private val mViewModel: MineViewModel by viewModels()
    private var mTvName: TextView? = null
    private var mTvCredits: TextView? = null
    private var mIvLogout: ImageView? = null
    private var mGroupUserInfo: Group? = null
    override fun initView(view: View) {
        super.initView(view)
        mTvName = view.findViewById(R.id.tvName)
        mTvCredits = view.findViewById(R.id.tvCredits)
        mIvLogout = view.findViewById(R.id.imgLogout)
        mGroupUserInfo = view.findViewById(R.id.groupUserInfo)
        mIvLogout?.setOnClickListener {
            mViewModel.logout()
        }
    }

    override fun onFirstInit() {
        super.onFirstInit()
        mViewModel.loadData()
    }

    override fun initObserver() {
        super.initObserver()
        observe(mViewModel.stateLiveData) {
            it?.user?.apply {
                mTvName?.text = "${username}"
                mGroupUserInfo?.visibility = View.VISIBLE
            }
            it?.coin?.apply {
                mTvCredits?.paint?.flags = Paint.UNDERLINE_TEXT_FLAG
                mTvCredits?.paint?.isAntiAlias = true
                mTvCredits?.text = "积分：$coinCount 排名：$rank"
            }
        }
    }
}