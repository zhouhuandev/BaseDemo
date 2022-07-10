package com.hzsoft.lib.net

import com.hzsoft.lib.domain.entity.Demo
import com.hzsoft.lib.net.dto.Resource
import com.hzsoft.lib.net.local.LocalData
import com.hzsoft.lib.net.local.entity.UserTestRoom
import com.hzsoft.lib.net.remote.BaseRemoteData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlin.coroutines.CoroutineContext


/**
 * 基础公共数据仓库进行分发
 * * 服务端
 * * 本地
 * @author zhouhuan
 * @time 2020/12/1 0:21
 */
open class BaseDataRepository constructor(
    private val commonRemoteRepository: BaseRemoteData = BaseRemoteData(),
    private val commonLocalRepository: LocalData = LocalData(),
    protected val ioDispatcher: CoroutineContext = Dispatchers.IO
) :
    BaseDataRepositorySource {

    override suspend fun requestRecipes(): Flow<Resource<List<Demo>>> {
        return dealDataFlow { commonRemoteRepository.requestRecipes() }
    }

    override suspend fun doLogin(): Flow<Resource<String>> {
        return dealDataFlow { commonLocalRepository.doLogin() }
    }

    override suspend fun removeUserTestRoom(userTestRoom: UserTestRoom): Flow<Resource<Int>> {
        return dealDataFlow { commonLocalRepository.removeUserTestRoom(userTestRoom) }
    }

    override suspend fun insertUserTestRoom(userTestRoom: UserTestRoom): Flow<Resource<Long>> {
        return dealDataFlow { commonLocalRepository.insertUserTestRoom(userTestRoom) }
    }

    override suspend fun getAllUserTestRoom(): Flow<Resource<List<UserTestRoom>>> {
        return dealDataFlow { commonLocalRepository.getUserTestRoom() }
    }

    protected inline fun <reified T> dealDataFlow(crossinline block: suspend () -> T): Flow<T> {
        return flow {
            emit(block.invoke())
        }.flowOn(ioDispatcher)
    }
}
