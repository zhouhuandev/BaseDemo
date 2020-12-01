package com.hzsoft.lib.net

import com.hzsoft.lib.net.dto.Demo
import com.hzsoft.lib.net.dto.Resource
import com.hzsoft.lib.net.local.LocalData
import com.hzsoft.lib.net.remote.RemoteData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext


/**
 * 数据仓库进行分发
 * * 服务端
 * * 本地
 * @author zhouhuan
 * @time 2020/12/1 0:21
 */
class DataRepository @Inject constructor(
    private val remoteRepository: RemoteData,
    private val localRepository: LocalData,
    private val ioDispatcher: CoroutineContext
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
}
