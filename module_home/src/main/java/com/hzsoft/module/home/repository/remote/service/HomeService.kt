package com.hzsoft.module.home.repository.remote.service

import com.hzsoft.lib.domain.base.BaseResponse
import com.hzsoft.lib.domain.entity.Demo
import com.hzsoft.lib.net.remote.service.BaseApiService
import retrofit2.Response
import retrofit2.http.GET

/**
 *
 *
 * @author <a href="mailto:zhouhuandev@gmail.com" rel="nofollow">zhouhuan</a>
 * @since 2022/7/10 11:50
 */
interface HomeService : BaseApiService {
    @GET("beauty_star.json")
    suspend fun getBeautyStar(): Response<BaseResponse<List<Demo>>>
}