package com.hzsoft.lib.net.utils

import com.hzsoft.lib.domain.base.BaseBean
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody

/**
 * Retrofit转换请求工具类
 * @author zhouhuan
 * @time 2021/6/25 21:05
 */
object RequestBodyUtil {

    @JvmStatic
    fun createRequestBody(params: Map<*, *>): RequestBody {
        return RequestBody.create(
            "application/json; charset=utf-8".toMediaType(),
            params.toJson()
        )
    }

    @JvmStatic
    fun <T : BaseBean> createRequestBody(bean: T): RequestBody {
        return RequestBody.create(
            "application/json; charset=utf-8".toMediaType(),
            bean.toJson()
        )
    }

    @JvmStatic
    fun <T : BaseBean> createRequestBody(list: List<T>): RequestBody {
        return RequestBody.create(
            "application/json; charset=utf-8".toMediaType(),
            list.toJson()
        )
    }

    @JvmStatic
    fun <T> createRequestBodyNew(list: List<T>): RequestBody {
        return RequestBody.create(
            "application/json; charset=utf-8".toMediaType(),
            list.toJson()
        )
    }

    @JvmStatic
    fun <T> createFieldMap(bean: T): Map<String, Any> {
        return MoshiUtils.mapFromJson(bean!!.toJson());
    }
}