package com.hzsoft.lib.net.remote.network

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit

/**
 *
 *
 * @author <a href="mailto:zhouhuandev@gmail.com" rel="nofollow">zhouhuan</a>
 * @since 2024/3/14 17:24
 */
interface IRetrofit {
    fun getOkHttpClientBuilder(): OkHttpClient.Builder

    fun getLoggerInterceptor(): Interceptor

    fun getRetrofit(): Retrofit
}