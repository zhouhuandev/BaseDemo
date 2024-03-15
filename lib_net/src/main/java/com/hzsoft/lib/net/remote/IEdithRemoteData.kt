package com.hzsoft.lib.net.remote

import com.hzsoft.lib.domain.entity.Demo
import com.hzsoft.lib.net.dto.Resource

/**
 * Edith 服务（域名）提供的请求接口
 *
 * @author <a href="mailto: zhouhuandev@gmail.com" rel="nofollow">zhouhuan</a>
 * @since 2024/3/14
 */
interface IEdithRemoteData {
    suspend fun requestRecipes(): Resource<List<Demo>?>
}
