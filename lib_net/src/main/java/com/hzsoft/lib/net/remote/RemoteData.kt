package com.task.data.remote

import com.hzsoft.lib.net.error.NETWORK_ERROR
import com.hzsoft.lib.net.error.NO_INTERNET_CONNECTION
import com.hzsoft.lib.net.remote.service.RecipesService
import com.hzsoft.lib.net.remote.service.ServiceGenerator
import com.hzsoft.lib.net.utils.NetworkConnectivity
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject


/**
 *
 * @author zhouhuan
 * @time 2020/12/1 0:08
 */
class RemoteData @Inject
constructor(
    private val serviceGenerator: ServiceGenerator,
    private val networkConnectivity: NetworkConnectivity
) : RemoteDataSource {
    override suspend fun requestRecipes(): String {
        val recipesService = serviceGenerator.createService(RecipesService::class.java)
        return when (val response = processCall(recipesService::fetchRecipes)) {
            is List<*> -> {
                ""
            }
            else -> {
                ""
            }
        }
    }

    private suspend fun processCall(responseCall: suspend () -> Response<*>): Any? {
        if (!networkConnectivity.isConnected()) {
            return NO_INTERNET_CONNECTION
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
            NETWORK_ERROR
        }
    }
}
