package com.hzsoft.lib.net.remote

import com.fly.tour.common.util.log.KLog
import com.hzsoft.lib.common.utils.ext.view.showToast
import com.hzsoft.lib.domain.base.BaseResponse
import com.hzsoft.lib.domain.entity.Demo
import com.hzsoft.lib.net.BuildConfig
import com.hzsoft.lib.net.config.NetAppContext
import com.hzsoft.lib.net.dto.Resource
import com.hzsoft.lib.net.error.ApiException
import com.hzsoft.lib.net.error.NETWORD_ERROR
import com.hzsoft.lib.net.error.mapper.ErrorManager
import com.hzsoft.lib.net.error.mapper.ErrorMapper
import com.hzsoft.lib.net.remote.service.RecipesService
import com.hzsoft.lib.net.utils.NetworkConnectivity
import com.task.data.remote.RemoteDataSource
import retrofit2.Response
import java.io.IOException


/**
 * 服务端数据提供者实现
 * @author zhouhuan
 * @time 2020/12/1 0:08
 */
class RemoteData
constructor(
    private val retrofitManager: RetrofitManager,
    private val networkConnectivity: NetworkConnectivity
) : RemoteDataSource {
    val TAG = this::class.java.simpleName
    val errorManager by lazy { ErrorManager(ErrorMapper()) }

    override suspend fun requestRecipes(): Resource<List<Demo>> {
        //创建接口服务
        val recipesService = retrofitManager.create<RecipesService>()

        return when (val response = processCall(recipesService::fetchRecipes)) {
            is BaseResponse<*> -> {
                Resource.Success(data = response.data as ArrayList<Demo>)
            }
            else -> {
                Resource.DataError(errorCode = response as Int)
            }
        }
    }


    /**
     * 数据结构体的返回处理
     */
    private suspend fun processCall(responseCall: suspend () -> Response<*>): Any? {
        if (!networkConnectivity.isConnected()) {
            return NETWORD_ERROR
        }
        return try {
            val response = responseCall.invoke()
            val responseCode = response.code()
            // 拦截器里面已经处理过此处，故此处不需要二次处理
            if (response.isSuccessful) {
                response.body()
            } else {
                // 网络响应不成功返回
                val ex = ApiException(responseCode)
                ex.message = errorManager.getError(responseCode).description
                ex.message.showToast(NetAppContext.getContext())
                KLog.e(TAG, ex.message, ex)
                throw ex
            }
        } catch (e: IOException) {
            if (BuildConfig.DEBUG) {
                e.message?.showToast(NetAppContext.getContext())
                e.printStackTrace()
            }
            NETWORD_ERROR
        }
    }
}
