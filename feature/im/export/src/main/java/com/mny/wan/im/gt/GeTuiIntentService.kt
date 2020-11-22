package com.mny.wan.im.gt

import android.content.Context
import android.os.Message
import android.util.Log
import com.blankj.utilcode.util.LogUtils
import com.igexin.sdk.GTIntentService
import com.igexin.sdk.PushConsts
import com.igexin.sdk.PushManager
import com.igexin.sdk.message.*
import com.mny.wan.im.R

/**
 * 继承 GTIntentService 接收来自个推的消息, 所有消息在线程中回调, 如果注册了该服务, 则务必要在 AndroidManifest中声明, 否则无法接受消息<br>
 * onReceiveMessageData 处理透传消息<br>
 * onReceiveClientId 接收 cid <br>
 * onReceiveOnlineState cid 离线上线通知 <br>
 * onReceiveCommandResult 各种事件处理回执 <br>
 */
class GeTuiIntentService : GTIntentService() {
    private val TAG = "GetuiSdkDemo"

    /**
     * 为了观察透传数据变化.
     */
    private var cnt = 0

    override fun onReceiveServicePid(context: Context?, pid: Int) {
        LogUtils.v("onReceiveServicePid -> $pid")
    }

    override fun onReceiveMessageData(context: Context?, msg: GTTransmitMessage) {
        val appid = msg.appid
        val taskid = msg.taskId
        val messageid = msg.messageId
        val payload = msg.payload
        val pkg = msg.pkgName
        val cid = msg.clientId

        // 第三方回执调用接口，actionid范围为90000-90999，可根据业务场景执行
        val result = PushManager.getInstance().sendFeedbackMessage(context, taskid, messageid, 90001)
        LogUtils.v("call sendFeedbackMessage = " + if (result) "success" else "failed")
        LogUtils.v("""
     onReceiveMessageData -> appid = $appid
     taskid = $taskid
     messageid = $messageid
     pkg = $pkg
     cid = $cid
     """.trimIndent())
        if (payload == null) {
            Log.e(TAG, "receiver payload = null")
        } else {
            var data = String(payload)
            Log.d(TAG, "receiver payload = $data")

            // 测试消息为了观察数据变化
            if (data == "收到一条透传测试消息") {
                data = "$data-$cnt"
                cnt++
            }
//            sendMessage(data, DemoApplication.DemoHandler.RECEIVE_MESSAGE_DATA)
        }
        Log.d(TAG, "----------------------------------------------------------------------------------------------")
    }

    override fun onReceiveClientId(context: Context?, clientid: String) {
        LogUtils.v("onReceiveClientId -> clientid = $clientid")
//        sendMessage(clientid, DemoApplication.DemoHandler.RECEIVE_CLIENT_ID)
    }

    override fun onReceiveOnlineState(context: Context?, online: Boolean) {
        LogUtils.v("onReceiveOnlineState -> " + if (online) "online" else "offline")
//        sendMessage(online.toString(), DemoApplication.DemoHandler.RECEIVE_ONLINE_STATE)
    }

    override fun onReceiveCommandResult(context: Context?, cmdMessage: GTCmdMessage) {
        LogUtils.v("onReceiveCommandResult -> $cmdMessage")
        val action = cmdMessage.action
        if (action == PushConsts.SET_TAG_RESULT) {
            setTagResult(cmdMessage as SetTagCmdMessage)
        } else if (action == PushConsts.BIND_ALIAS_RESULT) {
            bindAliasResult(cmdMessage as BindAliasCmdMessage)
        } else if (action == PushConsts.UNBIND_ALIAS_RESULT) {
            unbindAliasResult(cmdMessage as UnBindAliasCmdMessage)
        } else if (action == PushConsts.THIRDPART_FEEDBACK) {
            feedbackResult(cmdMessage as FeedbackCmdMessage)
        }
    }

    override fun onNotificationMessageArrived(context: Context?, message: GTNotificationMessage) {
        Log.d(TAG, """
     onNotificationMessageArrived -> appid = ${message.appid}
     taskid = ${message.taskId}
     messageid = ${message.messageId}
     pkg = ${message.pkgName}
     cid = ${message.clientId}
     title = ${message.title}
     content = ${message.content}
     """.trimIndent())
    }

    override fun onNotificationMessageClicked(context: Context?, message: GTNotificationMessage) {
        LogUtils.v("""
     onNotificationMessageClicked -> appid = ${message.appid}
     taskid = ${message.taskId}
     messageid = ${message.messageId}
     pkg = ${message.pkgName}
     cid = ${message.clientId}
     title = ${message.title}
     content = ${message.content}
     """.trimIndent())
    }

    private fun setTagResult(setTagCmdMsg: SetTagCmdMessage) {
        val sn = setTagCmdMsg.sn
        val code = setTagCmdMsg.code
        var text: Int = R.string.add_tag_unknown_exception
        when (code.toInt()) {
            PushConsts.SETTAG_SUCCESS -> text = R.string.add_tag_success
            PushConsts.SETTAG_ERROR_COUNT -> text = R.string.add_tag_error_count
            PushConsts.SETTAG_ERROR_FREQUENCY -> text = R.string.add_tag_error_frequency
            PushConsts.SETTAG_ERROR_REPEAT -> text = R.string.add_tag_error_repeat
            PushConsts.SETTAG_ERROR_UNBIND -> text = R.string.add_tag_error_unbind
            PushConsts.SETTAG_ERROR_EXCEPTION -> text = R.string.add_tag_unknown_exception
            PushConsts.SETTAG_ERROR_NULL -> text = R.string.add_tag_error_null
            PushConsts.SETTAG_NOTONLINE -> text = R.string.add_tag_error_not_online
            PushConsts.SETTAG_IN_BLACKLIST -> text = R.string.add_tag_error_black_list
            PushConsts.SETTAG_NUM_EXCEED -> text = R.string.add_tag_error_exceed
            PushConsts.SETTAG_TAG_ILLEGAL -> text = R.string.add_tag_error_tagIllegal
            else -> {
            }
        }
//        sendMessage(resources.getString(text), DemoApplication.DemoHandler.SHOW_TOAST)
        LogUtils.v("settag result sn = " + sn + ", code = " + code + ", text = " + resources.getString(text))
    }

    private fun bindAliasResult(bindAliasCmdMessage: BindAliasCmdMessage) {
        val sn = bindAliasCmdMessage.sn
        val code = bindAliasCmdMessage.code
        var text: Int = R.string.bind_alias_unknown_exception
        when (code.toInt()) {
            PushConsts.BIND_ALIAS_SUCCESS -> text = R.string.bind_alias_success
            PushConsts.ALIAS_ERROR_FREQUENCY -> text = R.string.bind_alias_error_frequency
            PushConsts.ALIAS_OPERATE_PARAM_ERROR -> text = R.string.bind_alias_error_param_error
            PushConsts.ALIAS_REQUEST_FILTER -> text = R.string.bind_alias_error_request_filter
            PushConsts.ALIAS_OPERATE_ALIAS_FAILED -> text = R.string.bind_alias_unknown_exception
            PushConsts.ALIAS_CID_LOST -> text = R.string.bind_alias_error_cid_lost
            PushConsts.ALIAS_CONNECT_LOST -> text = R.string.bind_alias_error_connect_lost
            PushConsts.ALIAS_INVALID -> text = R.string.bind_alias_error_alias_invalid
            PushConsts.ALIAS_SN_INVALID -> text = R.string.bind_alias_error_sn_invalid
            else -> {
            }
        }
//        sendMessage(resources.getString(text), DemoApplication.DemoHandler.SHOW_TOAST)
        LogUtils.v("bindAlias result sn = " + sn + ", code = " + code + ", text = " + resources.getString(text))
    }

    private fun unbindAliasResult(unBindAliasCmdMessage: UnBindAliasCmdMessage) {
        val sn = unBindAliasCmdMessage.sn
        val code = unBindAliasCmdMessage.code
        var text: Int = R.string.unbind_alias_unknown_exception
        when (code.toInt()) {
            PushConsts.UNBIND_ALIAS_SUCCESS -> text = R.string.unbind_alias_success
            PushConsts.ALIAS_ERROR_FREQUENCY -> text = R.string.unbind_alias_error_frequency
            PushConsts.ALIAS_OPERATE_PARAM_ERROR -> text = R.string.unbind_alias_error_param_error
            PushConsts.ALIAS_REQUEST_FILTER -> text = R.string.unbind_alias_error_request_filter
            PushConsts.ALIAS_OPERATE_ALIAS_FAILED -> text = R.string.unbind_alias_unknown_exception
            PushConsts.ALIAS_CID_LOST -> text = R.string.unbind_alias_error_cid_lost
            PushConsts.ALIAS_CONNECT_LOST -> text = R.string.unbind_alias_error_connect_lost
            PushConsts.ALIAS_INVALID -> text = R.string.unbind_alias_error_alias_invalid
            PushConsts.ALIAS_SN_INVALID -> text = R.string.unbind_alias_error_sn_invalid
            else -> {
            }
        }
//        sendMessage(resources.getString(text), DemoApplication.DemoHandler.SHOW_TOAST)
        LogUtils.v("unbindAlias result sn = " + sn + ", code = " + code + ", text = " + resources.getString(text))
    }


    private fun feedbackResult(feedbackCmdMsg: FeedbackCmdMessage) {
        val appid = feedbackCmdMsg.appid
        val taskid = feedbackCmdMsg.taskId
        val actionid = feedbackCmdMsg.actionId
        val result = feedbackCmdMsg.result
        val timestamp = feedbackCmdMsg.timeStamp
        val cid = feedbackCmdMsg.clientId
        LogUtils.v("""
     onReceiveCommandResult -> appid = $appid
     taskid = $taskid
     actionid = $actionid
     result = $result
     cid = $cid
     timestamp = $timestamp
     """.trimIndent())
    }

    private fun sendMessage(data: String, what: Int) {
        val msg = Message.obtain()
        msg.what = what
        msg.obj = data
//        DemoApplication.sendMessage(msg)
    }
}