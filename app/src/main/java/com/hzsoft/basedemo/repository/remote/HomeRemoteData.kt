package com.hzsoft.basedemo.repository.remote

import com.hzsoft.basedemo.repository.remote.service.HomeService
import com.hzsoft.lib.domain.entity.Demo
import com.hzsoft.lib.net.dto.Resource
import com.hzsoft.lib.net.remote.remotedata.MainRemoteData

/**
 * Home 远端仓库
 *
 * @author <a href="mailto:zhouhuandev@gmail.com" rel="nofollow">zhouhuan</a>
 * @since 2022/7/10 11:40
 */
class HomeRemoteData : MainRemoteData(), HomeDataSource {

    private val homeService by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { retrofitManager.create<HomeService>() }

    override suspend fun getBeautyStar(): Resource<List<Demo>?> {
        return convertWrapper(processCall { homeService.getBeautyStar() })
    }
}