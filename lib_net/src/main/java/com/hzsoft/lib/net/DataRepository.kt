package com.hzsoft.lib.net

import com.hzsoft.lib.domain.entity.Demo
import com.hzsoft.lib.net.config.NetAppContext
import com.hzsoft.lib.net.dto.Resource
import com.hzsoft.lib.net.local.LocalData
import com.hzsoft.lib.net.local.entity.UserTestRoom
import com.hzsoft.lib.net.remote.RemoteData
import com.hzsoft.lib.net.remote.RetrofitManager
import com.hzsoft.lib.net.utils.NetworkHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlin.coroutines.CoroutineContext


/**
 * 数据仓库进行分发
 * * 服务端
 * * 本地
 * @author zhouhuan
 * @time 2020/12/1 0:21
 */
class DataRepository constructor(
    private val remoteRepository: RemoteData = RemoteData(
        RetrofitManager.instance, NetworkHelper.instance
    ),
    private val localRepository: LocalData = LocalData(),
    private val ioDispatcher: CoroutineContext = Dispatchers.IO
) :
    DataRepositorySource {

    override suspend fun requestRecipes(): Flow<Resource<List<Demo>>> {
        return dealDataFlow { remoteRepository.requestRecipes() }
    }

    override suspend fun doLogin(): Flow<Resource<String>> {
        return dealDataFlow { localRepository.doLogin() }
    }

    override suspend fun removeUserTestRoom(userTestRoom: UserTestRoom): Flow<Resource<Int>> {
        return dealDataFlow { localRepository.removeUserTestRoom(userTestRoom) }
    }

    override suspend fun insertUserTestRoom(userTestRoom: UserTestRoom): Flow<Resource<Long>> {
        return dealDataFlow { localRepository.insertUserTestRoom(userTestRoom) }
    }

    override suspend fun getAllUserTestRoom(): Flow<Resource<List<UserTestRoom>>> {
        return dealDataFlow { localRepository.getUserTestRoom() }
    }

    private inline fun <reified T> dealDataFlow(crossinline block: suspend () -> T): Flow<T> {
        return flow {
            emit(block.invoke())
        }.flowOn(ioDispatcher)
    }
}
