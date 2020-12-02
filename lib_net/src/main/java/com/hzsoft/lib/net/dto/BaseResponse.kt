package com.hzsoft.lib.net.dto

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
class BaseResponse<T> constructor(
    @Json(name = "success")
    var success: Boolean = false,
    @Json(name = "status")
    var status: Int = 0,
    @Json(name = "message")
    var message: String? = null,
    @Json(name = "data")
    var data: T? = null
)