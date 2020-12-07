package com.hzsoft.lib.net

import com.hzsoft.lib.domain.entity.Demo
import com.hzsoft.lib.net.config.NetAppContext
import com.hzsoft.lib.net.dto.Resource
import com.hzsoft.lib.net.local.LocalData
import com.hzsoft.lib.net.local.entity.UserTestRoom
import com.hzsoft.lib.net.remote.RemoteData
import com.hzsoft.lib.net.remote.RetrofitManager
import com.hzsoft.lib.net.utils.Network
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
        RetrofitManager(), Network(NetAppContext.getContext())
    ),
    private val localRepository: LocalData = LocalData(),
    private val ioDispatcher: CoroutineContext = Dispatchers.IO
) :
    DataRepositorySource {

    override suspend fun requestRecipes(): Flow<Resource<List<Demo>>> {
        return flow {
            emit(remoteRepository.requestRecipes())
        }.flowOn(ioDispatcher)
    }

    override suspend fun doLogin(): Flow<Resource<String>> {
        return flow {
            emit(localRepository.doLogin())
        }.flowOn(ioDispatcher)
    }

    override suspend fun removeFromFavourite(id: String): Flow<Resource<Boolean>> {
        return flow {
            emit(localRepository.removeFromFavourites(id))
        }.flowOn(ioDispatcher)
    }

    override suspend fun insertUserTestRoom(userTestRoom: UserTestRoom): Flow<Resource<Long>> {
        return flow {
            emit(localRepository.inserUserTestRoom(userTestRoom))
        }.flowOn(ioDispatcher)
    }

    override suspend fun getAllUserTestRoom(): Flow<Resource<List<UserTestRoom>>> {
        return flow {
            emit(localRepository.getUserTestRoom())
        }.flowOn(ioDispatcher)
    }


}
