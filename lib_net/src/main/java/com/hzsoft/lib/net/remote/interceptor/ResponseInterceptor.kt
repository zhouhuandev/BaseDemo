package com.hzsoft.lib.net.remote.interceptor

import android.text.TextUtils
import com.fly.tour.common.util.log.KLog
import com.hzsoft.lib.common.utils.showToast
import com.hzsoft.lib.net.config.Encoding
import com.hzsoft.lib.net.config.NetAppContext
import com.hzsoft.lib.net.config.contentTypeValue
import com.hzsoft.lib.net.dto.BaseResponse
import com.hzsoft.lib.net.error.*
import com.hzsoft.lib.net.error.UNKNOWN
import com.hzsoft.lib.net.error.mapper.ErrorManager
import com.task.data.error.mapper.ErrorMapper
import com.wx.jetpack.core.utils.fromJson
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Response
import okhttp3.ResponseBody
import java.nio.charset.Charset

/**
 * Describe:
 * <p>相应数据拦截器</p>
 *
 * @author zhouhuan
 * @Date 2020/12/1
 */
class ResponseInterceptor : Interceptor {
    private val TAG = ResponseInterceptor::class.java.simpleName
    val errorManager: ErrorManager = ErrorManager(ErrorMapper())

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)
        val body = bufferBody(response)

        // 网络相应不成功返回
        if (!response.isSuccessful) {
            val ex = ApiException(response.code)
            ex.message = errorManager.getError(response.code).description
            ex.message.showToast(NetAppContext.getContext())
            KLog.e(TAG, ex.message, ex)
            throw ex
        }

        return try {
            if (TextUtils.isEmpty(body) || "null".equals(body, ignoreCase = true)) {
                throw ApiException(NULL_DATA, errorManager.getError(NULL_DATA).description)
            }
            // TODO: 2020/12/1 处理返回的结构体
            val fromJson = body.fromJson<BaseResponse<String>>()
            val status = fromJson!!.status
            if (SUCCESS == status) {
                response.newBuilder()
                    .body(ResponseBody.create(contentTypeValue.toMediaTypeOrNull(), body))
                    .build()
            } else {
                throw ApiException(status, fromJson.message ?: "")
            }
        } catch (e: Exception) {
            throw ApiException(PARSE_ERROR, errorManager.getError(PARSE_ERROR).description)
        }
    }

    fun bufferBody(response: Response): String {
        val source = response.body!!.source()
        source.request(Long.MAX_VALUE)
        val buffer = source.buffer()
        return buffer.clone().readString(Charset.forName(Encoding))
    }
}