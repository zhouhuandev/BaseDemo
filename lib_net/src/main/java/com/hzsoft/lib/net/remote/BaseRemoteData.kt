package com.hzsoft.lib.net.remote

import android.text.TextUtils
import com.hzsoft.lib.base.utils.ext.view.showToast
import com.hzsoft.lib.domain.base.BaseResponse
import com.hzsoft.lib.domain.entity.Demo
import com.hzsoft.lib.log.KLog
import com.hzsoft.lib.net.BuildConfig
import com.hzsoft.lib.net.dto.Resource
import com.hzsoft.lib.net.error.*
import com.hzsoft.lib.net.error.mapper.ErrorManager
import com.hzsoft.lib.net.error.mapper.ErrorMapper
import com.hzsoft.lib.net.remote.service.RecipesService
import com.hzsoft.lib.net.utils.NetworkConnectivity
import com.hzsoft.lib.net.utils.NetworkHelper
import retrofit2.Response


/**
 * 服务端数据提供者实现
 * @author zhouhuan
 * @time 2020/12/1 0:08
 */
open class BaseRemoteData
constructor(
    protected val retrofitManager: RetrofitManager = RetrofitManager.instance,
    private val networkConnectivity: NetworkConnectivity = NetworkHelper.instance
) : BaseRemoteDataSource {
    private val errorManager by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { ErrorManager(ErrorMapper()) }
    private val recipesService by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { retrofitManager.create<RecipesService>() }

    override suspend fun requestRecipes(): Resource<List<Demo>> {
        return dealDataWhen(processCall(recipesService::fetchRecipes))
    }

    /**
     * 数据结构体的返回处理
     */
    protected suspend fun processCall(responseCall: suspend () -> Response<*>): Any? {
        if (!networkConnectivity.isConnected()) {
            //若当前客户端未打开数据连接开关
            return showToast(NOT_NETWORD)
        }
        return try {
            val response = responseCall.invoke()
            if (response.code() in SUCCESS until UNAUTHORIZED) {
                response.body()
            } else {
                when (response.code()) {
                    UNAUTHORIZED -> showToast(UNAUTHORIZED)
                    FORBIDDEN -> showToast(FORBIDDEN)
                    NOT_FOUND -> showToast(NOT_FOUND)
                    REQUEST_TIMEOUT -> showToast(REQUEST_TIMEOUT)
                    INTERNAL_SERVER_ERROR -> showToast(INTERNAL_SERVER_ERROR)
                    SERVICE_UNAVAILABLE -> showToast(SERVICE_UNAVAILABLE)
                    else -> showToast(UNKNOWN)
                }
            }
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
                KLog.e("RemoteData", e)
            }
            showToast(NETWORD_ERROR)
        }
    }

    /**
     * 处理相应结果
     */
    protected inline fun <reified T> dealDataWhen(any: Any?): Resource<T> {
        return when (any) {
            is BaseResponse<*> -> {
                Resource.Success(data = toAs(if (any.data != null) any.data else null))
            }
            else -> {
                Resource.DataError(UNKNOWN)
            }
        }
    }

    /**
     * 类型转换
     */
    protected inline fun <reified T> toAs(obj: Any?): T {
        return obj as T
    }

    /**
     * 错误吐司
     */
    private fun showToast(code: Int, msg: String? = ""): Int {
        if (!TextUtils.isEmpty(msg)) msg?.showToast()
        else errorManager.getError(code).description.showToast()
        return code
    }
}
