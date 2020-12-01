package com.hzsoft.lib.net.remote.interceptor

import android.util.Log
import com.hzsoft.lib.net.config.NetConfig
import com.hzsoft.lib.net.config.contentType
import com.hzsoft.lib.net.config.contentTypeValue
import okhttp3.Interceptor
import okhttp3.Response

/**
 * Describe:
 * <p>请求头拦截器</p>
 *
 * @author zhouhuan
 * @Date 2020/12/1
 */
class RequestInterceptor : Interceptor {
    private val TAG = RequestInterceptor::class.java.simpleName

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()

        val url = original.url.toString()

        val requestBuiler = original.newBuilder()
            // .removeHeader("User-Agent")
            // .addHeader("User-Agent", "dh-net-okhttp")
            .header(contentType, contentTypeValue)
            .method(original.method, original.body)
        //设置请求头
        val heads = NetConfig.getHeads()
        if (!heads.isEmpty()) {
            for (head in heads) {
                requestBuiler.addHeader(head.key, head.value.toString())
            }
        }

        val build = requestBuiler.build()
        Log.d(TAG, "intercept:${build.headers}")
        return chain.proceed(build)
    }
}