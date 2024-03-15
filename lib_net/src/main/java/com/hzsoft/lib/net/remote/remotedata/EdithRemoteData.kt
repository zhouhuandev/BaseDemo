package com.hzsoft.lib.net.remote.remotedata

import com.hzsoft.lib.domain.entity.Demo
import com.hzsoft.lib.net.config.URL_EDITH
import com.hzsoft.lib.net.dto.Resource
import com.hzsoft.lib.net.remote.IEdithRemoteData
import com.hzsoft.lib.net.remote.network.AbsRemoteDataSource
import com.hzsoft.lib.net.remote.network.RetrofitManager
import com.hzsoft.lib.net.remote.service.RecipesService


/**
 * 服务（域名）实现接口请求实现类
 *
 * @author <a href="mailto: zhouhuandev@gmail.com" rel="nofollow">zhouhuan</a>
 * @since 2024/3/14
 */
open class EdithRemoteData(
    protected val retrofitManager: RetrofitManager = RetrofitManager.getInstance(URL_EDITH),
) : AbsRemoteDataSource(), IEdithRemoteData {
    private val recipesService by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { retrofitManager.create<RecipesService>() }

    override suspend fun requestRecipes(): Resource<List<Demo>?> {
        val processCall = processCall { recipesService.fetchRecipes() }
        return convertWrapper(processCall)
    }

    companion object {
        private const val TAG = "EdithRemoteDataImpl"
    }
}