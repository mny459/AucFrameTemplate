package com.mny.mojito.http.log

import okhttp3.*
import okio.Buffer
import java.io.IOException
import java.io.UnsupportedEncodingException
import java.nio.charset.Charset
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * 日志拦截器
 */
class LogInterceptor @Inject constructor() : Interceptor {
    @Inject
    lateinit var mPrinter: FormatPrinter

    @Inject
    lateinit var mCurPrintLevel: Level

    enum class Level {
        /**
         * 不打印log
         */
        NONE,

        /**
         * 只打印请求信息
         */
        REQUEST,

        /**
         * 只打印响应信息
         */
        RESPONSE,

        /**
         * 所有数据全部打印
         */
        ALL
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val logRequest = mCurPrintLevel == Level.ALL || mCurPrintLevel == Level.REQUEST

        if (logRequest) {
            //打印请求信息
            if (request.body != null && isParsable(request.body!!.contentType())) {
                mPrinter.printJsonRequest(request, parseParams(request))
            } else {
                mPrinter.printFileRequest(request)
            }
        }

        val logResponse = mCurPrintLevel == Level.ALL || mCurPrintLevel == Level.RESPONSE
        val t1 = if (logResponse) System.nanoTime() else 0
        val originalResponse: Response
        originalResponse = try {
            chain.proceed(request)
        } catch (e: Exception) {
//            Timber.w("Http Error: " + e);
            throw e
        }
        val t2 = if (logResponse) System.nanoTime() else 0

        val responseBody = originalResponse.body

        //打印响应结果

        //打印响应结果
        var bodyString: String? = null
        if (responseBody != null && isParsable(responseBody.contentType())) {
            bodyString = printResult(request, originalResponse, logResponse)
        }

        if (logResponse) {
            val segmentList = request.url.encodedPathSegments
            val header = originalResponse.headers.toString()
            val code = originalResponse.code
            val isSuccessful = originalResponse.isSuccessful
            val message = originalResponse.message
            val url = originalResponse.request.url.toString()
            if (responseBody != null && isParsable(responseBody.contentType())) {
                mPrinter.printJsonResponse(TimeUnit.NANOSECONDS.toMillis(t2 - t1), isSuccessful,
                        code, header, responseBody.contentType(), bodyString, segmentList, message, url)
            } else {
                mPrinter.printFileResponse(TimeUnit.NANOSECONDS.toMillis(t2 - t1),
                        isSuccessful, code, header, segmentList, message, url)
            }
        }


        return originalResponse
    }

    /**
     * 打印响应结果
     *
     * @param request     [Request]
     * @param response    [Response]
     * @param logResponse 是否打印响应结果
     * @return 解析后的响应结果
     * @throws IOException
     */
    @Throws(IOException::class)
    private fun printResult(request: Request, response: Response, logResponse: Boolean): String? {
        return try {
            //读取服务器返回的结果
            val responseBody = response.newBuilder().build().body
            val source = responseBody!!.source()
            source.request(Long.MAX_VALUE) // Buffer the entire body.
            val buffer = source.buffer()

            //获取content的压缩类型
            val encoding = response
                    .headers["Content-Encoding"]
            val clone = buffer.clone()

            //解析response content
            parseContent(responseBody, encoding, clone)
        } catch (e: IOException) {
            e.printStackTrace()
            "{\"error\": \"" + e.message + "\"}"
        }
    }

    /**
     * 解析服务器响应的内容
     *
     * @param responseBody [ResponseBody]
     * @param encoding     编码类型
     * @param clone        克隆后的服务器响应内容
     * @return 解析后的响应结果
     */
    private fun parseContent(responseBody: ResponseBody?, encoding: String?, clone: Buffer): String? {
        var charset = Charset.forName("UTF-8")
        val contentType = responseBody!!.contentType()
        if (contentType != null) {
            charset = contentType.charset(charset)
        }
        //        if (encoding != null && encoding.equalsIgnoreCase("gzip")) {//content 使用 gzip 压缩
//            return ZipHelper.decompressForGzip(clone.readByteArray(), convertCharset(charset));//解压
//        } else if (encoding != null && encoding.equalsIgnoreCase("zlib")) {//content 使用 zlib 压缩
//            return ZipHelper.decompressToStringForZlib(clone.readByteArray(), convertCharset(charset));//解压
//        } else {//content 没有被压缩, 或者使用其他未知压缩方式
//            return clone.readString(charset);
//        }
        return clone.readString(charset)
    }

    /**
     * 解析请求服务器的请求参数
     *
     * @param request [Request]
     * @return 解析后的请求信息
     * @throws UnsupportedEncodingException
     */
    @Throws(UnsupportedEncodingException::class)
    fun parseParams(request: Request): String {
        return try {
            val body = request.newBuilder().build().body ?: return ""
            val requestbuffer = Buffer()
            body.writeTo(requestbuffer)
            var charset = Charset.forName("UTF-8")
            val contentType = body.contentType()
            if (contentType != null) {
                charset = contentType.charset(charset)
            }
            //            if (UrlEncoderUtils.hasUrlEncoded(json)) {
//                json = URLDecoder.decode(json, convertCharset(charset));
//            }
//            return CharacterHandler.jsonFormat(json);
            requestbuffer.readString(charset)
        } catch (e: IOException) {
            e.printStackTrace()
            "{\"error\": \"" + e.message + "\"}"
        }
    }


    companion object {
        /**
         * 是否可以解析
         *
         * @param mediaType [MediaType]
         * @return `true` 为可以解析
         */
        fun isParsable(mediaType: MediaType?): Boolean {
            return if (mediaType?.type == null) false
            else isText(mediaType) || isPlain(mediaType)
                    || isJson(mediaType) || isForm(mediaType)
                    || isHtml(mediaType) || isXml(mediaType)
        }

        private fun isText(mediaType: MediaType): Boolean {
            return mediaType.type == "text"
        }

        private fun isPlain(mediaType: MediaType): Boolean {
            return mediaType.subtype.toLowerCase().contains("plain")
        }

        private fun isJson(mediaType: MediaType): Boolean {
            return mediaType.subtype.toLowerCase().contains("json")
        }

        private fun isXml(mediaType: MediaType): Boolean {
            return mediaType.subtype.toLowerCase().contains("xml")
        }

        private fun isHtml(mediaType: MediaType): Boolean {
            return mediaType.subtype.toLowerCase().contains("html")
        }

        private fun isForm(mediaType: MediaType): Boolean {
            return mediaType.subtype.toLowerCase().contains("x-www-form-urlencoded")
        }

        private fun convertCharset(charset: Charset): String? {
            val s = charset.toString()
            val i = s.indexOf("[")
            return if (i == -1) s else s.substring(i + 1, s.length - 1)
        }

    }
}