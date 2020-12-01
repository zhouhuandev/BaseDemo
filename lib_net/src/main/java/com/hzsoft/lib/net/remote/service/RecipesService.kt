package com.hzsoft.lib.net.remote.service

import com.hzsoft.lib.net.dto.Demo
import retrofit2.Response
import retrofit2.http.GET

/**
 * 服务端提供数据接口方法
 * @author zhouhuan
 * @time 2020/11/30
 */
interface RecipesService {
    @GET("recipes.json")
    suspend fun fetchRecipes(): Response<List<Demo>>
}