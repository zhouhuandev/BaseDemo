package com.hzsoft.lib.net.utils

import com.hzsoft.lib.domain.base.BaseBean
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

/**
 * Retrofit转换请求工具类
 * @author zhouhuan
 * @time 2021/6/25 21:05
 */
object RequestBodyUtil {

    @JvmStatic
    fun createRequestBody(params: Map<*, *>): RequestBody {
        return params.toJson()
            .toRequestBody("application/json; charset=utf-8".toMediaType())
    }

    @JvmStatic
    fun <T : BaseBean> createRequestBody(bean: T): RequestBody {
        return bean.toJson()
            .toRequestBody("application/json; charset=utf-8".toMediaType())
    }

    @JvmStatic
    fun <T : BaseBean> createRequestBody(list: List<T>): RequestBody {
        return list.toJson()
            .toRequestBody("application/json; charset=utf-8".toMediaType())
    }

    @JvmStatic
    fun <T> createRequestBodyNew(list: List<T>): RequestBody {
        return list.toJson()
            .toRequestBody("application/json; charset=utf-8".toMediaType())
    }

    @JvmStatic
    fun <T> createFieldMap(bean: T): Map<String, Any>? {
        return GsonUtils.mapFromJson(bean?.toJson())
    }
}