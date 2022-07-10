package com.hzsoft.basedemo.repository.remote

import com.hzsoft.lib.domain.entity.Demo
import com.hzsoft.lib.net.dto.Resource

/**
 *
 *
 * @author <a href="mailto:zhouhuandev@gmail.com" rel="nofollow">zhouhuan</a>
 * @since 2022/7/10 11:53
 */
interface HomeDataSource {
    suspend fun getBeautyStar(): Resource<List<Demo>>
}