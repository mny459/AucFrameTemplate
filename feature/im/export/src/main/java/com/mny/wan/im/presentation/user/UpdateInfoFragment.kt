package com.mny.wan.im.presentation.user

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ResourceUtils
import com.blankj.utilcode.util.ToastUtils
import com.bumptech.glide.Glide
import com.mny.wan.base.BaseFragment
import com.mny.wan.im.R
import com.mny.wan.im.extension.getPortraitTmpFile
import com.mny.wan.im.extension.showLoading
import com.mny.wan.im.presentation.media.GalleryFragment
import com.mny.wan.entension.observe
import com.mny.wan.entension.setOnDebouncedClickListener
import com.mny.wan.mvvm.BaseViewState
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog
import com.yalantis.ucrop.UCrop
import dagger.hilt.android.AndroidEntryPoint
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.fragment_update_info.*
import java.io.File

@AndroidEntryPoint
class UpdateInfoFragment : BaseFragment(R.layout.fragment_update_info) {

    private val mViewModel: UpdateInfoViewModel by viewModels()
    private var mLoadingDialog: QMUITipDialog? = null
    private var mPortraitPath: String? = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1600529210227&di=fb08aa6b4ed3ac5fb13ee98ebc72f703&imgtype=0&src=http%3A%2F%2Finews.gtimg.com%2Fnewsapp_bt%2F0%2F5714852882%2F1000"
    private var mIsMan = true

    private val mViewStateObserver = Observer<BaseViewState> {
        when {
            it.loading -> {
                mLoadingDialog = mActivity?.showLoading("登录中")
            }
            it.complete -> {
                mLoadingDialog?.hide()
                if (it.errorMsg.isNotEmpty()) {
                    ToastUtils.showShort("登录成功")
                } else {
                    ToastUtils.showShort(it.errorMsg)
                }
            }
        }
    }

    override fun initView(view: View) {
        super.initView(view)
        observe(mViewModel.viewStateLiveData, mViewStateObserver)
        view.findViewById<CircleImageView>(R.id.iv_portrait)?.setOnDebouncedClickListener {
            GalleryFragment.newInstance()
                    .setListener(object : GalleryFragment.OnSelectedListener {
                        override fun onSelectedImage(path: String) {
                            val options = UCrop.Options()
                            // 设置图片处理的格式JPEG
                            options.setCompressionFormat(Bitmap.CompressFormat.JPEG)
                            // 设置压缩后的图片精度
                            options.setCompressionQuality(96)
                            // 得到头像的缓存地址
                            val dPath: File = getPortraitTmpFile()
                            // 发起剪切
                            UCrop.of(Uri.fromFile(File(path)), Uri.fromFile(dPath))
                                    .withAspectRatio(1f, 1f) // 1比1比例
                                    .withMaxResultSize(520, 520) // 返回最大的尺寸
                                    .withOptions(options) // 相关参数
                                    .start(mActivity!!, this@UpdateInfoFragment)
                        }
                    })
                    .show(childFragmentManager, GalleryFragment::class.java.name)
            // show 的时候建议使用getChildFragmentManager，
            // tag GalleryFragment class 名
        }
        view.findViewById<Button>(R.id.btn_submit)?.setOnClickListener {
            val desc = et_desc.text.toString().trim()
            mViewModel.updateUserInfo(mPortraitPath, mIsMan, desc)
        }
        val ivGender = view.findViewById<ImageView>(R.id.iv_gender)
        ivGender?.setOnClickListener {
            this.mIsMan = !this.mIsMan
            val drawable = ResourceUtils.getDrawable(if (this.mIsMan) R.drawable.ic_sex_man else R.drawable.ic_sex_woman)
            ivGender.setImageDrawable(drawable)
            // TODO 理解这个 level 的作用
            ivGender.background?.level = if (mIsMan) 0 else 1
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // 收到从Activity传递过来的回调，然后取出其中的值进行图片加载
        // 如果是我能够处理的类型
        if (resultCode == Activity.RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            // 通过UCrop得到对应的Uri
            val resultUri = UCrop.getOutput(data!!)
            resultUri?.let { loadPortrait(it) }
        } else if (resultCode == UCrop.RESULT_ERROR) {
            val cropError = UCrop.getError(data!!)
            ToastUtils.showShort(R.string.data_rsp_error_unknown)
        }
    }


    /**
     * 加载Uri到当前的头像中
     *
     * @param uri Uri
     */
    private fun loadPortrait(uri: Uri) {
        mPortraitPath = uri.path
        Glide.with(this)
                .load(uri)
                .centerCrop()
                .into(iv_portrait)

        // 拿到本地文件的地址
        val localPath = uri.path
        LogUtils.e("localPath:$localPath")
//        Factory.runOnAsync(Runnable {
//            val url: String = UploadHelper.uploadPortrait(localPath)
//            Log.e("TAG", "url:$url")
//        })
    }

    companion object {
        fun newInstance() = UpdateInfoFragment()
    }
}