package com.hzsoft.lib.net.remote

import com.hzsoft.lib.domain.entity.Demo
import com.hzsoft.lib.net.dto.Resource
import com.hzsoft.lib.net.error.NETWORD_ERROR
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

    override suspend fun requestRecipes(): Resource<List<Demo>> {
        //创建接口服务
        val recipesService = retrofitManager.create<RecipesService>()

        return when (val response = processCall(recipesService::fetchRecipes)) {
            is com.hzsoft.lib.domain.base.BaseResponse<*> -> {
                Resource.Success(data = response.data as ArrayList<com.hzsoft.lib.domain.entity.Demo>)
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
            if (response.isSuccessful) {
                response.body()
            } else {
                responseCode
            }
        } catch (e: IOException) {
            NETWORD_ERROR
        }
    }
}
