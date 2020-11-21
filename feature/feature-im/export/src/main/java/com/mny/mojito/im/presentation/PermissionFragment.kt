package com.mny.mojito.im.presentation

import android.Manifest
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.blankj.utilcode.util.StringUtils
import com.blankj.utilcode.util.ToastUtils
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mny.mojito.im.R
import com.mny.mojito.im.widget.TransStatusBottomSheetDialog
import com.mny.mojito.entension.setOnDebouncedClickListener
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions

class PermissionFragment : BottomSheetDialogFragment(), EasyPermissions.PermissionCallbacks {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // 获取布局中的控件
        val root: View = inflater.inflate(R.layout.fragment_permission, container, false)
        // 找到按钮
        root.findViewById<View>(R.id.btn_submit)
                .setOnDebouncedClickListener {
                    // 点击时进行申请权限
                    requestPerm()
                }

        return root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return TransStatusBottomSheetDialog(requireContext())
    }

    /**
     * 申请权限的方法
     */
    @AfterPermissionGranted(RC)
    private fun requestPerm() {
        val perms = arrayOf(
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO
        )
        if (EasyPermissions.hasPermissions(requireContext(), *perms)) {
            ToastUtils.showShort(R.string.label_permission_ok)
            // Fragment 中调用getView得到跟布局，前提是在onCreateView方法之后
            refreshState(view)
        } else {
            EasyPermissions.requestPermissions(this, StringUtils.getString(R.string.title_assist_permissions), RC, *perms)
        }
    }

    override fun onResume() {
        super.onResume()
        // 界面显示的时候进行刷新
        refreshState(view)
    }

    /**
     * 刷新我们的布局中的图片的状态
     *
     * @param root 跟布局
     */
    private fun refreshState(root: View?) {
        if (root == null) return
        val context = requireContext()
        root.findViewById<View>(R.id.im_state_permission_network).visibility = if (haveNetworkPerm(context)) View.VISIBLE else View.GONE
        root.findViewById<View>(R.id.im_state_permission_read).visibility = if (haveReadPerm(context)) View.VISIBLE else View.GONE
        root.findViewById<View>(R.id.im_state_permission_write).visibility = if (haveWritePerm(context)) View.VISIBLE else View.GONE
        root.findViewById<View>(R.id.im_state_permission_record_audio).visibility = if (haveRecordAudioPerm(context)) View.VISIBLE else View.GONE
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        mOnPermissionGrantedListener?.onGranted()
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        // 如果权限有没有申请成功的权限存在，则弹出弹出框，用户点击后去到设置界面自己打开权限
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this)
                    .build()
                    .show()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        // 传递对应的参数，并且告知接收权限的处理者是我自己
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    companion object {
        // 权限回调的标示
        private const val RC = 0x0100
        private var mOnPermissionGrantedListener: OnPermissionGrantedListener? = null

        /**
         * 检查是否具有所有的权限
         *
         * @param context Context
         * @param manager FragmentManager
         * @return 是否有权限
         */
        fun haveAll(context: Context, manager: FragmentManager, onPermissionGrantedListener: OnPermissionGrantedListener): Boolean {
            mOnPermissionGrantedListener = onPermissionGrantedListener
            // 检查是否具有所有的权限
            val haveAll = haveNetworkPerm(context)
                    && haveReadPerm(context)
                    && haveWritePerm(context)
                    && haveRecordAudioPerm(context)

            // 如果没有则显示当前申请权限的界面
            if (!haveAll) {
                show(manager)
            }
            return haveAll
        }

        // 私有的show方法
        private fun show(manager: FragmentManager) {
            // 调用BottomSheetDialogFragment以及准备好的显示方法
            newInstance().show(manager, PermissionFragment::class.java.name)
        }


        /**
         * 获取是否有网络权限
         *
         * @param context 上下文
         * @return True则有
         */
        private fun haveNetworkPerm(context: Context): Boolean {
            // 准备需要检查的网络权限
            val perms = arrayOf(
                    Manifest.permission.INTERNET,
                    Manifest.permission.ACCESS_NETWORK_STATE,
                    Manifest.permission.ACCESS_WIFI_STATE
            )
            return EasyPermissions.hasPermissions(context, *perms)
        }

        /**
         * 获取是否有外部存储读取权限
         *
         * @param context 上下文
         * @return True则有
         */
        private fun haveReadPerm(context: Context): Boolean {
            // 准备需要检查的读取权限
            val perms = arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE
            )
            return EasyPermissions.hasPermissions(context, *perms)
        }

        /**
         * 获取是否有外部存储写入权限
         *
         * @param context 上下文
         * @return True则有
         */
        private fun haveWritePerm(context: Context): Boolean {
            // 准备需要检查的写入权限
            val perms = arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            return EasyPermissions.hasPermissions(context, *perms)
        }

        /**
         * 获取是否录音权限
         *
         * @param context 上下文
         * @return True则有
         */
        private fun haveRecordAudioPerm(context: Context): Boolean {
            // 准备需要检查的录音权限
            val perms = arrayOf(
                    Manifest.permission.RECORD_AUDIO
            )
            return EasyPermissions.hasPermissions(context, *perms)
        }

        @JvmStatic
        fun newInstance() = PermissionFragment()
    }

    interface OnPermissionGrantedListener {
        fun onGranted()
    }
}