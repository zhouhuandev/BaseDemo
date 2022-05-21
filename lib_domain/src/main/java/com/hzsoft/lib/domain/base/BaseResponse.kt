package com.hzsoft.lib.domain.base

/**
 * Describe:
 * <p>基础结构体</p>
 *
 * @author zhouhuan
 * @Date 2020/12/1
 */
class BaseResponse<out T> constructor(
    val code: Int = 0,
    val msg: String? = "",
    val data: T? = null
)