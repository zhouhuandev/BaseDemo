package com.hzsoft.module.home.repository

import com.hzsoft.lib.domain.entity.Demo
import com.hzsoft.lib.net.dto.Resource
import kotlinx.coroutines.flow.Flow

/**
 *
 *
 * @author <a href="mailto:zhouhuandev@gmail.com" rel="nofollow">zhouhuan</a>
 * @since 2022/7/10 12:10
 */
interface HomeDataRepositorySource {
    suspend fun getBeautyStar(): Flow<Resource<List<Demo>>>
}