package com.hzsoft.basedemo.repository

import com.hzsoft.lib.domain.entity.Demo
import com.hzsoft.lib.net.BaseDataRepository
import com.hzsoft.lib.net.dto.Resource
import com.hzsoft.basedemo.repository.remote.HomeRemoteData
import kotlinx.coroutines.flow.*

/**
 *
 *
 * @author <a href="mailto:zhouhuandev@gmail.com" rel="nofollow">zhouhuan</a>
 * @since 2022/7/10 11:47
 */
class HomeDataRepository : BaseDataRepository(), HomeDataRepositorySource {

    private val homeRemoteData by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { HomeRemoteData() }

    override suspend fun getBeautyStar(): Flow<Resource<List<Demo>>> {
        return dealDataFlow {
            val beautyStar = homeRemoteData.getBeautyStar()
            val requestRecipes = homeRemoteData.requestRecipes()
            val resource =
                if (beautyStar.isSuccess() || requestRecipes.isSuccess()) {
                    Resource.Success(arrayListOf<Demo>())
                } else {
                    Resource.DataError((beautyStar.errorCode + requestRecipes.errorCode) / 2)
                }
            if (beautyStar.isSuccess() && beautyStar.data?.isNotEmpty() == true) {
                beautyStar.data?.let {
                    resource.data?.addAll(it)
                }
            }
            if (resource.isSuccess() && requestRecipes.data?.isNotEmpty() == true) {
                requestRecipes.data?.let {
                    resource.data?.addAll(it)
                }
            }
            resource
        }
    }
}