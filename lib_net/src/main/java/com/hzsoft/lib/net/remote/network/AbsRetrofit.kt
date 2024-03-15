package com.hzsoft.lib.net.remote.network

import com.hzsoft.lib.net.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

/**
 *
 *
 * @author <a href="mailto:zhouhuandev@gmail.com" rel="nofollow">zhouhuan</a>
 * @since 2024/3/14 17:25
 */
abstract class AbsRetrofit : IRetrofit {

    override fun getOkHttpClientBuilder(): OkHttpClient.Builder {
        return OkHttpClient.Builder()
    }

    override fun getLoggerInterceptor(): Interceptor {
        val loggingInterceptor = HttpLoggingInterceptor()
        if (BuildConfig.DEBUG) {
            loggingInterceptor.apply { level = HttpLoggingInterceptor.Level.BODY }
        } else {
            loggingInterceptor.apply { level = HttpLoggingInterceptor.Level.BASIC }
        }
        return loggingInterceptor
    }

    fun <S> create(serviceClass: Class<S>): S = getRetrofit().create(serviceClass)

    inline fun <reified T> create(): T = create(T::class.java)
}