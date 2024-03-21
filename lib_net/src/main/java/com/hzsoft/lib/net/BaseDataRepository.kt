package com.hzsoft.lib.net

import com.hzsoft.lib.domain.entity.Demo
import com.hzsoft.lib.net.config.URL_EDITH
import com.hzsoft.lib.net.config.URL_MAIN
import com.hzsoft.lib.net.dto.Resource
import com.hzsoft.lib.net.local.LocalData
import com.hzsoft.lib.net.local.entity.UserTestRoom
import com.hzsoft.lib.net.remote.IEdithRemoteData
import com.hzsoft.lib.net.remote.IMainRemoteData
import com.hzsoft.lib.net.remote.remotedata.EdithRemoteData
import com.hzsoft.lib.net.remote.remotedata.MainRemoteData
import com.hzsoft.lib.net.remote.network.RetrofitManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
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
open class BaseDataRepository : BaseDataRepositorySource {

    protected val mMainRemote: IMainRemoteData by lazy {
        MainRemoteData(RetrofitManager.getInstance(URL_MAIN))
    }
    protected val mEdithRemote: IEdithRemoteData by lazy {
        EdithRemoteData(RetrofitManager.getInstance(URL_EDITH))
    }
    private val mCommonLocalRepository: LocalData = LocalData()
    protected val mIoDispatcher: CoroutineContext = Dispatchers.IO

    override suspend fun requestRecipesByMain(): Flow<Resource<List<Demo>?>> {
        return dealDataFlow { mMainRemote.requestRecipes() }
    }

    override suspend fun requestRecipesByEdith(): Flow<Resource<List<Demo>?>> {
        return dealDataFlow { mEdithRemote.requestRecipes() }
    }

    override suspend fun doLogin(): Flow<Resource<String>> {
        return dealDataFlow { mCommonLocalRepository.doLogin() }
    }

    override suspend fun removeUserTestRoom(userTestRoom: UserTestRoom): Flow<Resource<Int>> {
        return dealDataFlow { mCommonLocalRepository.removeUserTestRoom(userTestRoom) }
    }

    override suspend fun insertUserTestRoom(userTestRoom: UserTestRoom): Flow<Resource<Long>> {
        return dealDataFlow { mCommonLocalRepository.insertUserTestRoom(userTestRoom) }
    }

    override suspend fun getAllUserTestRoom(): Flow<Resource<List<UserTestRoom>>> {
        return dealDataFlow { mCommonLocalRepository.getUserTestRoom() }
    }

    override suspend fun getUserTestRoom(
        pageSize: Int,
        pageNumber: Int
    ): Flow<Resource<List<UserTestRoom>>> {
        return flow<Resource<List<UserTestRoom>>> {
            emit(Resource.Loading())
            delay((50..100).random().toLong())
            emit(mCommonLocalRepository.getUserTestRoom(pageSize, pageNumber))
        }.flowOn(mIoDispatcher)
    }

    protected inline fun <reified T> dealDataFlow(crossinline block: suspend () -> T): Flow<T> {
        return flow {
            emit(block.invoke())
        }.flowOn(mIoDispatcher)
    }
}
