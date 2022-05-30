package com.hzsoft.lib.net.error

import java.io.IOException

/**
 * Describe:
 * <p>接口异常</p>
 *
 * @author zhouhuan
 * @Date 2020/12/1
 */
class ApiException constructor(
    var code: Int,
    override var message: String = "",
    var e: Throwable? = null
) : IOException(message, e)