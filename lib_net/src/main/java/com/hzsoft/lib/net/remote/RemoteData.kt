package com.hzsoft.lib.net.remote

import com.hzsoft.lib.net.dto.Demo
import com.hzsoft.lib.net.dto.Resource
import com.hzsoft.lib.net.error.NETWORD_ERROR
import com.hzsoft.lib.net.remote.service.RecipesService
import com.hzsoft.lib.net.utils.NetworkConnectivity
import com.task.data.remote.RemoteDataSource
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject


/**
 * 服务端数据提供者
 * @author zhouhuan
 * @time 2020/12/1 0:08
 */
class RemoteData @Inject
constructor(
    private val serviceGenerator: RetrofitManager,
    private val networkConnectivity: NetworkConnectivity
) : RemoteDataSource {
    override suspend fun requestRecipes(): Resource<List<Demo>> {
        val recipesService = serviceGenerator.create(RecipesService::class.java)
        return when (val response = processCall(recipesService::fetchRecipes)) {
            is List<*> -> {
                Resource.Success(data = response as ArrayList<Demo>)
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
