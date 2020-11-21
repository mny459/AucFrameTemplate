package com.mny.mojito.im.data

import android.text.format.DateFormat
import com.alibaba.sdk.android.oss.OSS
import com.alibaba.sdk.android.oss.OSSClient
import com.alibaba.sdk.android.oss.common.OSSLog
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider
import com.alibaba.sdk.android.oss.model.PutObjectRequest
import com.blankj.utilcode.util.EncryptUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.Utils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.util.*

/**
 * 上传工具类，用于上传任意文件到阿里OSS存储
 *
 * @author qiujuer Email:qiujuer@live.cn
 * @version 1.0.0
 */
object UploadHelper {
    // 与你们的存储区域有关系
    private const val ENDPOINT = "http://oss-cn-shenzhen.aliyuncs.com"

    // 上传的仓库名
    private const val BUCKET_NAME = "ichat99"

    //    外网访问
    //oss-cn-shenzhen.aliyuncs.com
    //ichat99.oss-cn-shenzhen.aliyuncs.com
    //支持
    //ECS 的经典网络访问（内网）
    //oss-cn-shenzhen-internal.aliyuncs.com
    //ichat99.oss-cn-shenzhen-internal.aliyuncs.com
    //支持
    //ECS 的 VPC 网络访问（内网）
    //oss-cn-shenzhen-internal.aliyuncs.com
    //ichat99.oss-cn-shenzhen-internal.aliyuncs.com

    // 明文设置secret的方式建议只在测试时使用，更多鉴权模式请参考后面的`访问控制`章节
    private val client: OSS by lazy {
        OSSLog.enableLog()
        // 明文设置secret的方式建议只在测试时使用，更多鉴权模式请参考后面的`访问控制`章节
        val credentialProvider: OSSCredentialProvider = OSSPlainTextAKSKCredentialProvider(
                "LTAI4G6AQowNCV63afRjyYP9", "IO9WFl0DKDujFaQCiJxEQ6cM6POrsN")
        OSSClient(Utils.getApp(), ENDPOINT, credentialProvider)
    }

    /**
     * 上传的最终方法，成功返回则一个路径
     *
     * @param objKey 上传上去后，在服务器上的独立的KEY
     * @param path   需要上传的文件的路径
     * @return 存储的地址
     */
    private suspend fun upload(objKey: String, path: String): String? {
        return withContext(Dispatchers.IO) {
            // 构造一个上传请求
            val request = PutObjectRequest(BUCKET_NAME, objKey, path)
            try {
                // 初始化上传的Client
                val client = client
                // 开始同步上传
                val result = client.putObject(request)
                // 得到一个外网可访问的地址
                val url = client.presignPublicObjectURL(BUCKET_NAME, objKey)
                // 格式打印输出
                LogUtils.d(String.format("PublicObjectURL:%s", url))
                url
            } catch (e: Exception) {
                LogUtils.e("upload $e")
                e.printStackTrace()
                // 如果有异常则返回空
                null
            }
        }
    }

    /**
     * 上传普通图片
     *
     * @param path 本地地址
     * @return 服务器地址
     */
    suspend fun uploadImage(path: String): String? {
        val key = getImageObjKey(path)
        return upload(key, path)
    }

    /**
     * 上传头像
     *
     * @param path 本地地址
     * @return 服务器地址
     */
    suspend fun uploadPortrait(path: String): String? {
        val key = getPortraitObjKey(path)
        return upload(key, path)
    }

    /**
     * 上传音频
     *
     * @param path 本地地址
     * @return 服务器地址
     */
    suspend fun uploadAudio(path: String): String? {
        val key = getAudioObjKey(path)
        return upload(key, path)
    }

    /**
     * 分月存储，避免一个文件夹太多
     *
     * @return yyyyMM
     */
    private val dateString: String by lazy { DateFormat.format("yyyyMM", Date()).toString() }

    // image/201703/dawewqfas243rfawr234.jpg
    private fun getImageObjKey(path: String): String {
        val fileMd5 = EncryptUtils.encryptMD5File2String(File(path))
        val dateString = dateString
        return String.format("image/%s/%s.jpg", dateString, fileMd5)
    }

    // portrait/201703/dawewqfas243rfawr234.jpg
    private fun getPortraitObjKey(path: String): String {
        val fileMd5 = EncryptUtils.encryptMD5File2String(File(path))
        val dateString = dateString
        return String.format("avatar/%s/%s.jpg", dateString, fileMd5)
    }

    // audio/201703/dawewqfas243rfawr234.mp3
    private fun getAudioObjKey(path: String): String {
        val fileMd5 = EncryptUtils.encryptMD5File2String(File(path))
        val dateString = dateString
        return String.format("audio/%s/%s.mp3", dateString, fileMd5)
    }
}