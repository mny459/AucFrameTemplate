package com.mny.wan.im.presentation.message

import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.blankj.utilcode.util.ScreenUtils
import com.google.android.material.tabs.TabLayout
import com.mny.wan.base.BaseFragment
import com.mny.wan.im.R
import com.mny.wan.im.presentation.message.adapter.FaceAdapter
import com.mny.wan.im.widget.GalleryView
import com.mny.wan.im.widget.face.Face
import net.qiujuer.genius.ui.Ui
import java.io.File

class PanelFragment : BaseFragment(R.layout.fragment_panel) {
    private var mFacePanel: View? = null
    private var mGalleryPanel: View? = null
    private var mRecordPanel: View? = null
    private var mCallback: PanelCallback? = null
    override fun initView(view: View) {
        super.initView(view)

        initFace(view)
        initRecord(view)
        initGallery(view)
    }

    // 开始初始化方法
    fun setup(callback: PanelCallback) {
        mCallback = callback
    }

    private fun initFace(root: View) {
        val facePanel = root.findViewById<View>(R.id.lay_panel_face)
        mFacePanel = facePanel
        val backspace = facePanel.findViewById<View>(R.id.im_backspace)
        backspace.setOnClickListener(View.OnClickListener { // 删除逻辑
            val callback: PanelCallback = mCallback ?: return@OnClickListener

            // 模拟一个键盘删除点击
            val event = KeyEvent(0, 0, 0, KeyEvent.KEYCODE_DEL,
                    0, 0, 0, 0, KeyEvent.KEYCODE_ENDCALL)
            callback.getInputEditText()?.dispatchKeyEvent(event)
        })
        val tabLayout = facePanel.findViewById<TabLayout>(R.id.tab)
        val viewPager = facePanel.findViewById<ViewPager>(R.id.view_page)
        tabLayout.setupWithViewPager(viewPager)

        // 每一表情显示48dp
        val minFaceSize = Ui.dipToPx(resources, 48f).toInt()
        val totalScreen: Int = ScreenUtils.getScreenWidth()
        val spanCount = totalScreen / minFaceSize
        viewPager.adapter = object : PagerAdapter() {

            override fun isViewFromObject(view: View, `object`: Any): Boolean = view === `object`

            override fun getCount(): Int = Face.all(mActivity!!).size

            override fun instantiateItem(container: ViewGroup, position: Int): Any {
                // 添加的
                val inflater = LayoutInflater.from(context)
                val recyclerView: RecyclerView = inflater.inflate(R.layout.lay_face_content, container, false) as RecyclerView
                recyclerView.layoutManager = GridLayoutManager(context, spanCount)

                // 设置Adapter
                val faces: List<Face.Bean> = Face.all(mActivity!!)[position].faces
                val adapter = FaceAdapter(faces.toMutableList())
                adapter.setOnItemClickListener { _, _, position ->
                    if (mCallback != null) {
                        // 表情添加到输入框
                        val bean = adapter.data[position]
                        val editText = mCallback?.getInputEditText()
                        Face.inputFace(mActivity!!, editText?.text, bean, (editText?.textSize
                                ?: 0 + Ui.dipToPx(resources, 2f)).toInt())
                    }
                }
                recyclerView.adapter = adapter

                // 添加
                container.addView(recyclerView)
                return recyclerView
            }

            override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
                // 移除的
                container.removeView(`object` as View?)
            }

            override fun getPageTitle(position: Int): CharSequence? {
                // 拿到表情盘的描述
                return Face.all(mActivity!!)[position].name
            }
        }
    }

    private fun initRecord(root: View) {}

    private fun initGallery(root: View) {
        val galleryPanel = root.findViewById<View>(R.id.lay_gallery_panel).also { mGalleryPanel = it }
        val galleryView: GalleryView = galleryPanel.findViewById<GalleryView>(R.id.view_gallery)
        val selectedSize = galleryPanel.findViewById<TextView>(R.id.txt_gallery_select_count)

        galleryView.setup(loaderManager) { count ->
            val resStr = getText(R.string.label_gallery_selected_size).toString()
            selectedSize.text = String.format(resStr, count)
        }
        // 点击事件
        galleryPanel.findViewById<View>(R.id.btn_send).setOnClickListener { onGalleySendClick(galleryView, galleryView.getSelectedPath()) }
    }


    fun showFace() {
        mGalleryPanel?.visibility = View.GONE
        mFacePanel?.visibility = View.VISIBLE
    }


    // 点击的时候触发，传回一个控件和选中的路径
    private fun onGalleySendClick(galleryView: GalleryView, paths: Array<String>) {
        // 通知给聊天界面
        // 清理状态
        galleryView.clear()

        // 删除逻辑
        val callback = mCallback
                ?: return
        callback.onSendGallery(paths)
    }

    fun showRecord() {
        //mRecordPanel.setVisibility(View.VISIBLE);
        mGalleryPanel?.visibility = View.GONE
        mFacePanel?.visibility = View.GONE
    }

    fun showGallery() {
        //mRecordPanel.setVisibility(View.GONE);
        mGalleryPanel?.visibility = View.VISIBLE
        mFacePanel?.visibility = View.GONE
    }

    companion object {

        @JvmStatic
        fun newInstance() = PanelFragment()
    }

    // 回调聊天界面的Callback
    interface PanelCallback {
        fun getInputEditText(): EditText?

        // 返回需要发送的图片
        fun onSendGallery(paths: Array<String>)

        // 返回录音文件和时常
        fun onRecordDone(file: File, time: Long)
    }
}