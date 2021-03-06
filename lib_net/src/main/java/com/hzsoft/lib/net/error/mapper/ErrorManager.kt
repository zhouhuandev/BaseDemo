package com.hzsoft.lib.net.error.mapper

import com.hzsoft.lib.net.error.Error

/**
 * Describe:
 * <p>获取错误信息实现</p>
 *
 * @author zhouhuan
 * @Date 2020/12/1
 */
class ErrorManager constructor(private val errorMapper: ErrorMapper) : ErrorFactory {
    override fun getError(errorCode: Int): Error {
        return Error(code = errorCode, description = errorMapper.errorsMap.getValue(errorCode))
    }
}
