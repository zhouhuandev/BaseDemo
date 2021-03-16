package com.hzsoft.lib.net.remote

import com.hzsoft.lib.common.utils.ext.view.showToast
import com.hzsoft.lib.domain.base.BaseResponse
import com.hzsoft.lib.domain.entity.Demo
import com.hzsoft.lib.net.BuildConfig
import com.hzsoft.lib.net.config.NetAppContext
import com.hzsoft.lib.net.dto.Resource
import com.hzsoft.lib.net.error.NETWORD_ERROR
import com.hzsoft.lib.net.error.NOT_NETWORD
import com.hzsoft.lib.net.error.mapper.ErrorManager
import com.hzsoft.lib.net.error.mapper.ErrorMapper
import com.hzsoft.lib.net.remote.service.RecipesService
import com.hzsoft.lib.net.utils.NetworkConnectivity
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
            //若当前客户端未打开数据连接开关
            errorManager.getError(NOT_NETWORD).description.showToast(NetAppContext.getContext())
            return NOT_NETWORD
        }
        return try {
            val response = responseCall.invoke()
            //这里面不需要二次判断是否响应成功，已在响应拦截器里面处理
            response.body()
        } catch (e: IOException) {
            if (BuildConfig.DEBUG) {
                e.message?.showToast(NetAppContext.getContext())
                e.printStackTrace()
            }
            NETWORD_ERROR
        }
    }
}
