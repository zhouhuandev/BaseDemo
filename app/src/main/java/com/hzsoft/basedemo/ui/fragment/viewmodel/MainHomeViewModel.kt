package com.hzsoft.basedemo.ui.fragment.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.hzsoft.lib.base.mvvm.viewmodel.BaseRefreshViewModel
import com.hzsoft.lib.domain.entity.Demo
import com.hzsoft.lib.log.KLog
import com.hzsoft.lib.net.dto.Resource
import com.hzsoft.lib.net.local.entity.UserTestRoom
import com.hzsoft.lib.net.utils.toJson
import com.hzsoft.basedemo.repository.HomeDataRepository
import kotlinx.coroutines.launch

/**
 * Describe:
 * <p></p>
 *
 * @author zhouhuan
 * @Date 2020/12/1
 */
class MainHomeViewModel(state: SavedStateHandle) : BaseRefreshViewModel() {
    private val TAG = "MainHomeViewModel"

    val savedStateHandle = state

    private val homeDataRepository by lazy { HomeDataRepository() }

    private val recipesLiveDataPrivate = MutableLiveData<Resource<List<Demo>>>()
    val recipesLiveData: LiveData<Resource<List<Demo>>> get() = recipesLiveDataPrivate

    private val userTestRoomLiveDataPrivate = MutableLiveData<Resource<List<UserTestRoom>>>()
    val userTestRoomLiveData: LiveData<Resource<List<UserTestRoom>>> get() = userTestRoomLiveDataPrivate

    var itemCount = 0

    private fun getRecipes(isRefresh: Boolean) {
        viewModelScope.launch {
            // 合并请求结果示例
            homeDataRepository.getBeautyStar().collect {
                // setValue() 只能在主线程中调用，postValue() 可以在任何线程中调用
                // recipesLiveDataPrivate.value = it
                recipesLiveDataPrivate.postValue(it)
                if (isRefresh) {
                    postStopRefreshEvent()
                } else {
                    postStopLoadMoreEvent()
                }
                if (itemCount > 50) {
                    postShowToastViewEvent("数据全部加载完毕")
                    postStopLoadMoreWithNoMoreDataEvent()
                }
            }
            // 单个请求示例
            /*homeDataRepository.requestRecipes().collect {
                KLog.d(TAG, it.toJson())
            }*/
        }
    }

    override fun refreshData() {
        getRecipes(true)
    }

    override fun loadMore() {
        getRecipes(false)
    }

}
