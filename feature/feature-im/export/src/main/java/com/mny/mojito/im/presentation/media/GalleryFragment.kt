package com.mny.mojito.im.presentation.media

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mny.mojito.im.R
import com.mny.mojito.im.widget.GalleryView
import com.mny.mojito.im.widget.TransStatusBottomSheetDialog
import kotlinx.android.synthetic.main.fragment_gallery.*

class GalleryFragment : BottomSheetDialogFragment(), GalleryView.SelectedChangeListener {
    private var mListener: OnSelectedListener? = null
    private var mGalleryView: GalleryView? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return TransStatusBottomSheetDialog(requireContext())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_gallery, container, false)
        mGalleryView = root.findViewById(R.id.galleryView)
        return root
    }

    override fun onStart() {
        super.onStart()
        mGalleryView?.setup(loaderManager, this)
    }

    companion object {
        fun newInstance() = GalleryFragment()
    }

    override fun onSelectedCountChanged(count: Int) {
        // 如果选中的一张图片
        if (count > 0) {
            // 隐藏自己
            dismiss()
            if (mListener != null) {
                // 得到所有的选中的图片的路径
                val paths: Array<String> = galleryView.selectedPath
                // 返回第一张
                mListener?.onSelectedImage(paths[0])
                // 取消和唤起者之间的应用，加快内存回收
                mListener = null
            }
        }
    }

    /**
     * 设置事件监听，并返回自己
     *
     * @param listener OnSelectedListener
     * @return GalleryFragment
     */
    fun setListener(listener: OnSelectedListener) = apply {
        mListener = listener
    }

    /**
     * 选中图片的监听器
     */
    interface OnSelectedListener {
        fun onSelectedImage(path: String)
    }
}