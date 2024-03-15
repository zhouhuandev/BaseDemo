package com.hzsoft.lib.net.remote

import com.hzsoft.lib.domain.entity.Demo
import com.hzsoft.lib.net.dto.Resource

/**
 * Main 服务（域名）提供的请求接口
 * @author zhouhuan
 * @time 2020/12/1 0:06
 */
interface IMainRemoteData {
    suspend fun requestRecipes(): Resource<List<Demo>?>
}
