package com.hzsoft.lib.net.config

import okhttp3.Interceptor

/**
 * Describe:
 * <p>网络配置</p>
 *
 * @author zhouhuan
 * @Date 2020/12/1
 */
class NetConfig private constructor() {

    open class Builder {
        internal var baseUrl: String = ""
        internal var defaultTimeout: Long = 6_000
        internal var connectTimeout: Long = defaultTimeout
        internal var readTimeout: Long = defaultTimeout
        internal var writeTimeout: Long = defaultTimeout
        internal var retryOnConnectionFailure: Boolean = true
        internal var mToken: String = "";
        internal var interceptors = ArrayList<Interceptor>()
        internal var networkInterceptors = ArrayList<Interceptor>()
        internal var heads = HashMap<String, Any>()
        internal var enableHttps = false

        open fun setBaseUrl(baseUrl: String): Builder = apply {
            this.baseUrl = baseUrl
        }

        open fun setDefaultTimeout(defaultTimeout: Long): Builder = apply {
            this.defaultTimeout = defaultTimeout
        }

        open fun setConnectTimeout(connectTimeout: Long): Builder = apply {
            this.connectTimeout = connectTimeout
        }

        open fun setReadTimeout(readTimeout: Long): Builder = apply {
            this.readTimeout = readTimeout
        }

        open fun setWriteTimeout(writeTimeout: Long): Builder = apply {
            this.writeTimeout = writeTimeout
        }

        open fun setToken(token: String): Builder = apply {
            this.mToken = token
        }

        open fun addInterceptor(interceptor: Interceptor): Builder = apply {
            this.interceptors.add(interceptor)
        }

        open fun addNetworkInterceptors(interceptor: Interceptor): Builder = apply {
            this.networkInterceptors.add(interceptor)
        }

        open fun setHeads(headers: Map<String, Any>): Builder = apply {
            this.heads.putAll(headers)
        }

        open fun enableHttps(enableHttps: Boolean): Builder = apply {
            this.enableHttps = enableHttps
        }
    }
}