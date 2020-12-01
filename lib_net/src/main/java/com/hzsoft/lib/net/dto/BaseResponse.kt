package com.hzsoft.lib.net.dto

/**
 * Describe:
 * <p>基础结构体</p>
 *
 * @author zhouhuan
 * @Date 2020/12/1
 */
class BaseResponse<T> constructor(
    var success: Boolean = false,
    var status: Int = 0,
    var message: String? = null,
    var data: T? = null
)