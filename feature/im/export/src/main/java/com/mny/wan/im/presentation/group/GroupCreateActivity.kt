package com.mny.wan.im.presentation.group

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.ToastUtils
import com.bumptech.glide.Glide
import com.mny.wan.entension.observe
import com.mny.wan.im.R
import com.mny.wan.im.base.BaseToolbarActivity
import com.mny.wan.im.extension.getPortraitTmpFile
import com.mny.wan.im.presentation.group.adapter.CreateGroupMemberAdapter
import com.mny.wan.im.presentation.media.GalleryFragment
import com.mny.wan.im.widget.PortraitView
import com.yalantis.ucrop.UCrop
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import javax.inject.Inject

@AndroidEntryPoint
class GroupCreateActivity : BaseToolbarActivity() {
    private var mRvContacts: RecyclerView? = null
    private var mEtName: EditText? = null
    private var mEtDesc: EditText? = null
    private var mPortraitView: PortraitView? = null
    private var mPortraitPath: String = ""

    private val mViewModel by viewModels<GroupCreateViewModel>()

    @Inject
    lateinit var mAdapter: CreateGroupMemberAdapter

    override fun layoutId(savedInstanceState: Bundle?): Int = R.layout.activity_group_create

    override fun initObserver() {
        super.initObserver()
        observe(mViewModel.stateLiveData, ViewStateObserver())
    }

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        mRvContacts = findViewById(R.id.rv_contacts)
        mEtName = findViewById(R.id.et_name)
        mEtDesc = findViewById(R.id.et_desc)
        mPortraitView = findViewById(R.id.iv_portrait)

        title = ""
        mRvContacts?.layoutManager = LinearLayoutManager(mActivity)
        mRvContacts?.adapter = mAdapter
        mAdapter.setOnCheckChangedListener { member, isChecked ->
            mViewModel.onSelectUserChanged(member.user.serverId, isChecked)
        }
        mPortraitView?.setOnClickListener { showImagePicker() }
    }

    override fun initData(savedInstanceState: Bundle?) {
        super.initData(savedInstanceState)
        mViewModel.loadData()
    }

    private fun showImagePicker() {
        hideSoftKeyboard()
        GalleryFragment()
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
                                .start(this@GroupCreateActivity)
                    }
                }).show(supportFragmentManager, GalleryFragment::class.java.getName())
    }

    // 隐藏软件盘
    private fun hideSoftKeyboard() {
        // 当前焦点的View
        val view = currentFocus ?: return
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    /**
     * 加载Uri到当前的头像中
     *
     * @param uri Uri
     */
    private fun loadPortrait(uri: Uri) {
        // 得到头像地址
        mPortraitPath = uri.path ?: return
        mPortraitView?.apply {
            Glide.with(this)
                    .asBitmap()
                    .load(uri)
                    .centerCrop()
                    .into(this)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.group_create, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_create) {
            //  进行创建
            onCreateClick()
        }
        return super.onOptionsItemSelected(item)
    }

    // 进行创建操作
    private fun onCreateClick() {
        hideSoftKeyboard()
        val name: String = mEtName?.text.toString().trim { it <= ' ' }
        val desc: String = mEtDesc?.text.toString().trim { it <= ' ' }
        mViewModel.create(name, desc, "https://hbimg.huabanimg.com/a4302bc2a44898583fea2d117adcaedc626f356e4cc5d-sEweIP_fw658/format/webp")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // 收到从Activity传递过来的回调，然后取出其中的值进行图片加载
        // 如果是我能够处理的类型
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            // 通过UCrop得到对应的Uri
            val resultUri = UCrop.getOutput(data!!)
            resultUri?.let { loadPortrait(it) }
        } else if (resultCode == UCrop.RESULT_ERROR) {
            ToastUtils.showShort(R.string.data_rsp_error_unknown)
        }
    }


    inner class ViewStateObserver : Observer<GroupCreateViewModel.State> {
        override fun onChanged(t: GroupCreateViewModel.State?) {
            t?.apply {
                if (groupMembers.isNotEmpty()) {
                    mAdapter.data.clear()
                    mAdapter.addData(groupMembers)
                }
            }
        }

    }

    companion object {
        fun go(context: Context) {
            context.startActivity(Intent(context, GroupCreateActivity::class.java))
        }
    }
}