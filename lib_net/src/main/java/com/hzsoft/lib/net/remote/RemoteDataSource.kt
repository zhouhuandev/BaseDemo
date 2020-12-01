package com.task.data.remote

import com.hzsoft.lib.net.dto.Demo
import com.hzsoft.lib.net.dto.Resource

/**
 * 服务端所有提供数据方法
 * @author zhouhuan
 * @time 2020/12/1 0:06
 */
internal interface RemoteDataSource {
    suspend fun requestRecipes(): Resource<List<Demo>>
}
