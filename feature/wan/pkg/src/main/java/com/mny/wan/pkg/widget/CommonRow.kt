package com.mny.wan.pkg.widget

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.CompoundButton
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ScreenUtils
import com.blankj.utilcode.util.SizeUtils
import com.google.android.material.switchmaterial.SwitchMaterial
import com.mny.wan.pkg.R


class CommonRowView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    companion object {
        private val PADDING = SizeUtils.dp2px(16F)
        private val WIDTH = ScreenUtils.getScreenWidth()
        private val HEIGHT = SizeUtils.dp2px(56F)
    }

    private var mSubtitle: CharSequence?
    private var mSubtitleOn: CharSequence?
    private var mSubtitleOff: CharSequence?
    private var mShowSwitch: Boolean = false
    private val mTvTitle: TextView
    private val mTvSubtitle: TextView
    private val mSwitch: SwitchMaterial
    private var mOnCheckedChangeListener: CompoundButton.OnCheckedChangeListener? = null

    init {
        val a: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.CommonRowView)
        val title = a.getText(R.styleable.CommonRowView_title)
        mSubtitle = a.getText(R.styleable.CommonRowView_subtitle)
        mSubtitleOn = a.getText(R.styleable.CommonRowView_subtitleOn)
        mSubtitleOff = a.getText(R.styleable.CommonRowView_subtitleOff)
        mShowSwitch = a.getBoolean(R.styleable.CommonRowView_showSwitch, false)
        a.recycle()
        LayoutInflater.from(context).inflate(R.layout.cell_common_row, this)
        mTvTitle = findViewById(R.id.tv_title)
        mTvSubtitle = findViewById(R.id.tv_subtitle)
        mSwitch = findViewById(R.id.switch_row_switch)
        mTvTitle.text = title
        if (mSubtitle.isNullOrEmpty() && mSubtitleOn.isNullOrEmpty() && mSubtitleOff.isNullOrEmpty()) {
            mTvSubtitle.visibility = View.GONE
        }
        updateSubtitle()
        mSwitch.isClickable = false
        mSwitch.setOnCheckedChangeListener { buttonView: CompoundButton, isChecked: Boolean ->
            mOnCheckedChangeListener?.onCheckedChanged(buttonView, isChecked)
            updateSubtitle()
        }
//        setPadding(PADDING, 0, PADDING, 0)
    }

//    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
//        setMeasuredDimension(getDefaultSize(suggestedMinimumWidth, widthMeasureSpec), HEIGHT)
//    }

    private fun updateSubtitle() {
        if (!mShowSwitch) {
            mSwitch.visibility = View.GONE
            return
        }
        mTvSubtitle.text = getSubtitleText(mSwitch.isChecked)
    }

    fun setOnCheckedChangeListener(listener: CompoundButton.OnCheckedChangeListener) {
        this.mOnCheckedChangeListener = listener
    }

    private fun getSubtitleText(isChecked: Boolean): CharSequence {
        return if (isChecked && mSubtitleOn != null) {
            mSubtitleOn!!
        } else if (!isChecked && mSubtitleOff != null) {
            mSubtitleOff!!
        } else {
            mSubtitle ?: ""
        }
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        mSwitch.isEnabled = enabled
        mTvTitle.isEnabled = enabled
        mTvSubtitle.isEnabled = enabled
    }

    fun setChecked(checked: Boolean, log: Boolean) {
        if (isChecked() == checked) return
        mSwitch.isChecked = checked
        if (log) {
            LogUtils.d("$this setChecked $checked ${mSwitch.isChecked}")
        }
    }

    fun isChecked() = mSwitch.isChecked
}