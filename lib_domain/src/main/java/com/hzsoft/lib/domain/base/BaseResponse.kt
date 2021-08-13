package com.hzsoft.lib.domain.base

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Describe:
 * <p>基础结构体</p>
 *
 * @author zhouhuan
 * @Date 2020/12/1
 */
@JsonClass(generateAdapter = true)
class BaseResponse<out T> constructor(
    @Json(name = "code")
    val code: Int = 0,
    @Json(name = "msg")
    val msg: String? = "",
    @Json(name = "data")
    val data: T? = null
)