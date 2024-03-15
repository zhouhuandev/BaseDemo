package com.hzsoft.lib.net.remote.network

import android.text.TextUtils
import android.util.Log
import com.hzsoft.lib.base.utils.ext.view.showToast
import com.hzsoft.lib.domain.base.BaseResponse
import com.hzsoft.lib.log.KLog
import com.hzsoft.lib.net.BuildConfig
import com.hzsoft.lib.net.dto.Resource
import com.hzsoft.lib.net.error.FORBIDDEN
import com.hzsoft.lib.net.error.INTERNAL_SERVER_ERROR
import com.hzsoft.lib.net.error.NETWORK_ERROR
import com.hzsoft.lib.net.error.NOT_FOUND
import com.hzsoft.lib.net.error.NOT_NETWORK
import com.hzsoft.lib.net.error.REQUEST_TIMEOUT
import com.hzsoft.lib.net.error.SERVICE_UNAVAILABLE
import com.hzsoft.lib.net.error.SUCCESS
import com.hzsoft.lib.net.error.UNAUTHORIZED
import com.hzsoft.lib.net.error.mapper.ErrorManager
import com.hzsoft.lib.net.error.mapper.ErrorMapper
import com.hzsoft.lib.net.utils.NetworkConnectivity
import com.hzsoft.lib.net.utils.NetworkHelper
import retrofit2.Response

/**
 *
 *
 * @author <a href="mailto:zhouhuandev@gmail.com" rel="nofollow">zhouhuan</a>
 * @since 2024/3/14 17:56
 */
abstract class AbsRemoteDataSource {
    private val mErrorManager by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { ErrorManager(ErrorMapper()) }
    private val mNetworkConnectivity: NetworkConnectivity = NetworkHelper.instance
    private val mErrorCodeToasts = listOf(
        UNAUTHORIZED,
        FORBIDDEN,
        NOT_FOUND,
        REQUEST_TIMEOUT,
        INTERNAL_SERVER_ERROR,
        SERVICE_UNAVAILABLE,
        NETWORK_ERROR
    )

    /**
     * 数据结构体的返回处理
     */
    protected suspend fun <T> processCall(responseCall: suspend () -> Response<T>): Resource<T?> {
        var errorCode: Int
        var responseBody: T? = null
        if (!mNetworkConnectivity.isConnected()) {
            // 若当前客户端未打开数据连接开关
            errorCode = NOT_NETWORK
        } else {
            try {
                val response = responseCall.invoke()
                errorCode = response.code()
                responseBody = response.body()
            } catch (e: Throwable) {
                /**
                 * Kotlin 协程异常处理
                 * 使用协程及挂在函数请求，Retrofit 默认会使用 [retrofit2.HttpServiceMethod.SuspendForResponse] 处理请求，
                 * 而不是 [retrofit2.DefaultCallAdapterFactory.ExecutorCallbackCall]，
                 * 捕获异常是在 [okhttp3.internal.connection.RealCall] 的 run 方法中捕获 [java.io.IOException]中进行失败回调，
                 * 因此需要在 [retrofit2.HttpServiceMethod.SuspendForResponse] 返回异常，通过拓展函数 在 [retrofit2.KotlinExtensions.awaitResponse] 中捕获异常，并返回包装类，
                 * 在[kotlinx.coroutines.CancellableContinuationImpl.resumeImpl]中返回 CancelledContinuation 包装类，最终抛出异常
                 */
                if (BuildConfig.DEBUG) {
                    e.message?.showToast()
                    KLog.e(TAG, Log.getStackTraceString(e))
                }
                errorCode = NETWORK_ERROR
            }
        }
        val resource = if (errorCode in SUCCESS until UNAUTHORIZED) {
            Resource.Success(data = responseBody)
        } else {
            if (errorCode in mErrorCodeToasts) {
                showToast(errorCode)
            }
            Resource.DataError(errorCode)
        }
        return resource
    }


    /**
     * 处理相应结果
     */
    protected fun <T> convertWrapper(resource: Resource<BaseResponse<T>?>): Resource<T?> {
        return if (resource.isSuccess()) {
            Resource.Success(resource.data?.data)
        } else {
            Resource.DataError(resource.errorCode)
        }
    }

    /**
     * 错误吐司
     */
    private fun showToast(code: Int, msg: String? = ""): Int {
        if (!TextUtils.isEmpty(msg)) msg?.showToast()
        else mErrorManager.getError(code).description.showToast()
        return code
    }

    companion object {
        private const val TAG = "AbsRemoteDataSource"
    }
}