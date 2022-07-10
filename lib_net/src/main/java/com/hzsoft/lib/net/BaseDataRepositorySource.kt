package com.hzsoft.lib.net

import com.hzsoft.lib.domain.entity.Demo
import com.hzsoft.lib.net.dto.Resource
import com.hzsoft.lib.net.local.entity.UserTestRoom
import kotlinx.coroutines.flow.Flow


/**
 *
 * @author zhouhuan
 * @time 2020/12/1 0:21
 */
interface BaseDataRepositorySource {
    suspend fun requestRecipes(): Flow<Resource<List<Demo>>>
    suspend fun doLogin(): Flow<Resource<String>>
    suspend fun removeUserTestRoom(userTestRoom: UserTestRoom): Flow<Resource<Int>>
    suspend fun insertUserTestRoom(userTestRoom: UserTestRoom):Flow<Resource<Long>>
    suspend fun getAllUserTestRoom(): Flow<Resource<List<UserTestRoom>>>
}
