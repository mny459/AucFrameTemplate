package com.mny.wan.im.presentation.message


import android.text.TextUtils
import android.view.View
import android.view.ViewStub
import android.widget.EditText
import android.widget.ImageView
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.Toolbar
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.mny.mojito.base.BaseFragment
import com.mny.wan.im.R
import com.mny.wan.im.widget.MessageLayout
import com.mny.wan.im.widget.airpanel.AirPanel
import com.mny.wan.im.widget.airpanel.Util
import java.io.File

abstract class ChatFragment() : BaseFragment(R.layout.fragment_chat_user) {
    protected var mToolbar: Toolbar? = null
    protected var mRvMsg: RecyclerView? = null
    protected var mAppBarLayout: AppBarLayout? = null

    // 控制顶部面板与软键盘过度的Boss控件
    protected var mPanelBoss: AirPanel.Boss? = null
    protected var mPanelFragment: PanelFragment? = null
    protected var mIvFace: ImageView? = null
    protected var mIvRecord: ImageView? = null
    var mCollapsingLayout: CollapsingToolbarLayout? = null
    var mContent: EditText? = null
    var mSubmit: View? = null


    override fun initView(view: View) {
        // 拿到占位布局
        // 替换顶部布局一定需要发生在super之前
        // 防止控件绑定异常
        val stub = view.findViewById<ViewStub>(R.id.view_stub_header)
        stub.layoutResource = getHeaderLayoutId()
        stub.inflate()
        super.initView(view)
        mToolbar = view.findViewById(R.id.toolbar)
        mToolbar?.setNavigationIcon(R.drawable.ic_back)
        mToolbar?.setNavigationOnClickListener { mActivity?.finish() }

        mRvMsg = view.findViewById(R.id.rv_msg)
        mRvMsg?.layoutManager = LinearLayoutManager(mActivity)

        mAppBarLayout = view.findViewById(R.id.appbar)
        mCollapsingLayout = view.findViewById(R.id.collapsingToolbarLayout)
        mContent = view.findViewById(R.id.et_content)
        mSubmit = view.findViewById(R.id.iv_submit)

//        mAppBarLayout?.addOnOffsetChangedListener(AppbarLayoutListener())
        mContent?.addTextChangedListener(onTextChanged = { s, _, _, _ ->
            val content: String = s.toString().trim { it <= ' ' }
            val needSendMsg = !TextUtils.isEmpty(content)
            // 设置状态，改变对应的Icon
            mSubmit?.isActivated = needSendMsg
        })

        mSubmit?.setOnClickListener {
            if (it.isActivated) {
                val content = mContent?.text.toString().trim()
                mContent?.setText("")
                pushText(content)
            } else {
                onMoreClick()
            }
        }

        // 初始化面板操作

        mPanelBoss = view.findViewById<MessageLayout>(R.id.lay_content) as? AirPanel.Boss
        mPanelBoss?.setup(AirPanel.PanelListener { // 请求隐藏软键盘
            Util.hideKeyboard(mContent)
        })
        mPanelFragment = childFragmentManager.findFragmentById(R.id.frag_panel) as PanelFragment
        mPanelFragment?.setup(PanelCallback())

        mIvFace = view.findViewById(R.id.iv_face)
        mIvRecord = view.findViewById(R.id.iv_record)
        mIvFace?.setOnClickListener { onFaceClick() }
        mIvRecord?.setOnClickListener { onRecordClick() }
    }

    private fun onFaceClick() {
        // 仅仅只需请求打开即可
        mPanelBoss?.openPanel()
        mPanelFragment?.showFace()
    }

    private fun onRecordClick() {
        mPanelBoss?.openPanel()
        mPanelFragment?.showRecord()
    }

    private fun onMoreClick() {
        mPanelBoss?.openPanel()
        mPanelFragment?.showGallery()
    }

    abstract fun pushText(text: String)

    abstract fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int)

    // 得到顶部布局的资源Id
    @LayoutRes
    protected abstract fun getHeaderLayoutId(): Int

    inner class AppbarLayoutListener : AppBarLayout.OnOffsetChangedListener {
        override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
            onOffsetChanged(appBarLayout, verticalOffset)
        }
    }

    inner class PanelCallback : PanelFragment.PanelCallback {
        override fun getInputEditText(): EditText? = mContent
        override fun onSendGallery(paths: Array<String>) {
            // TODO 上传图片
        }

        override fun onRecordDone(file: File, time: Long) {
        }
    }
}