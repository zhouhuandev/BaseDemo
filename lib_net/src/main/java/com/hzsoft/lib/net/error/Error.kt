package com.hzsoft.lib.net.error

/**
 * 错误结构体
 * @author zhouhuan
 * @time 2020/12/1 0:08
 */
class Error(val code: Int, val description: String) {
    constructor(exception: Exception) : this(
        code = UNKNOWN,
        description = exception.message ?: ""
    )
}

const val SUCCESS = 200

/**
 * 服务器响应码304，但是客户端没有缓存
 */
const val NO_CACHE = 304

/**
 * 操作未授权
 */
const val UNAUTHORIZED = 401

/**
 * 拒绝请求
 */
const val FORBIDDEN = 403

/**
 * 资源不存在
 */
const val NOT_FOUND = 404

/**
 * 服务器执行超时
 */
const val REQUEST_TIMEOUT = 408

/**
 * 服务器内部错误
 */
const val INTERNAL_SERVER_ERROR = 500

/**
 * 服务器不可用
 */
const val SERVICE_UNAVAILABLE = 503

/**
 * 未知错误
 */
const val UNKNOWN = 1000

/**
 * 解析错误
 */
const val PARSE_ERROR = 1001

/**
 * SSL_ERROR
 * 网络错误
 */
const val NETWORD_ERROR = 1002

/**
 * 协议出错
 */
const val HTTP_ERROR = 1003

/**
 * 证书出错
 */
const val SSL_ERROR = 1005

/**
 * 连接超时
 */
const val TIMEOUT_ERROR = 1006

/**
 * 没有数据
 */
const val NULL_DATA = 1007
