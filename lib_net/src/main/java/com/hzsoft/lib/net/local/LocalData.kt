package com.hzsoft.lib.net.local

import com.hzsoft.lib.net.dto.Resource
import javax.inject.Inject

/**
 * 本地数据
 * @author zhouhuan
 * @time 2020/11/30
 */
class LocalData constructor() {
    fun doLogin(): Resource<String> {
        return Resource.Success("String")
    }

    fun removeFromFavourites(id: String): Resource<Boolean> {
        return Resource.Success(true)
    }
}