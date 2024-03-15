package com.hzsoft.lib.net.error.mapper

import com.hzsoft.lib.net.R
import com.hzsoft.lib.net.config.NetAppContext
import com.hzsoft.lib.net.error.*

/**
 * Describe:
 * <p>错误信息实现</p>
 *
 * @author zhouhuan
 * @Date 2020/12/1
 */
class ErrorMapper constructor() : ErrorMapperInterface {

    override fun getErrorString(errorId: Int): String {
        return NetAppContext.getContext().getString(errorId)
    }

    override val errorsMap: Map<Int, String>
        get() = mapOf(
            Pair(NO_CACHE, getErrorString(R.string.NO_CACHE)),
            Pair(UNAUTHORIZED, getErrorString(R.string.UNAUTHORIZED)),
            Pair(FORBIDDEN, getErrorString(R.string.FORBIDDEN)),
            Pair(NOT_FOUND, getErrorString(R.string.NOT_FOUND)),
            Pair(REQUEST_TIMEOUT, getErrorString(R.string.REQUEST_TIMEOUT)),
            Pair(INTERNAL_SERVER_ERROR, getErrorString(R.string.INTERNAL_SERVER_ERROR)),
            Pair(SERVICE_UNAVAILABLE, getErrorString(R.string.SERVICE_UNAVAILABLE)),
            Pair(PARSE_ERROR, getErrorString(R.string.PARSE_ERROR)),
            Pair(NETWORK_ERROR, getErrorString(R.string.NETWORD_ERROR)),
            Pair(TIMEOUT_ERROR, getErrorString(R.string.TIMEOUT_ERROR)),
            Pair(NULL_DATA, getErrorString(R.string.NULL_DATA)),
            Pair(NOT_NETWORK, getErrorString(R.string.NOT_NETWORD)),
            Pair(UNKNOWN, getErrorString(R.string.TIMEOUT_ERROR))
        ).withDefault { getErrorString(R.string.TIMEOUT_ERROR) }
}
