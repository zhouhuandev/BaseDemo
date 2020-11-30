package com.hzsoft.lib.net.remote.service

import retrofit2.Response
import retrofit2.http.GET

/**
 *
 * @author zhouhuan
 * @time 2020/11/30
 */
interface RecipesService {
    @GET("recipes.json")
    suspend fun fetchRecipes(): Response<List<String>>
}