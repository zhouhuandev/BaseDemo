package com.hzsoft.module.me.activity.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.hzsoft.lib.base.mvvm.viewmodel.BaseRefreshViewModel
import com.hzsoft.lib.domain.entity.Demo
import com.hzsoft.lib.net.BaseDataRepository
import com.hzsoft.lib.net.dto.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 *
 *
 * @author <a href="mailto:zhouhuandev@gmail.com" rel="nofollow">zhouhuan</a>
 * @since 2024/3/15 13:06
 */
class MoreRequestServerViewModel(state: SavedStateHandle) : BaseRefreshViewModel() {
    private val savedStateHandle = state

    private val dataRepositoryRepository by lazy { BaseDataRepository() }

    private val _recipesFlow = MutableStateFlow<Resource<List<Demo>?>>(Resource.Loading())
    val recipesFlow: StateFlow<Resource<List<Demo>?>> = _recipesFlow.asStateFlow()

    private var requestCount = 0 // 计数器，记录请求次数

    var itemCount = 0

    /**
     * 每次触发刷新或者加载更多的时候都会自动切换数据源
     * 数据源来自于两个不同域名的服务
     *
     * @param isRefresh
     */
    private fun getRecipes(isRefresh: Boolean) {
        viewModelScope.launch {
            // 根据请求次数的奇偶性来确定 random 的值
            val random = if (requestCount and 1 == 0) {
                0
            } else {
                1
            }
            requestCount++ // 增加请求次数
            val recipes: Flow<Resource<List<Demo>?>> = if (random == 1) {
                dataRepositoryRepository.requestRecipesByMain()
            } else {
                dataRepositoryRepository.requestRecipesByEdith()
            }
            recipes.collect {
                // 将结果发送到 StateFlow
                _recipesFlow.value = it
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
        }
    }

    override fun refreshData() {
        getRecipes(true)
    }

    override fun loadMore() {
        getRecipes(false)
    }
}