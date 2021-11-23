package com.hzsoft.moudule.home.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hzsoft.lib.base.mvvm.viewmodel.BaseRefreshViewModel
import com.hzsoft.lib.domain.entity.Demo
import com.hzsoft.lib.net.DataRepository
import com.hzsoft.lib.net.DataRepositorySource
import com.hzsoft.lib.net.dto.Resource
import com.hzsoft.lib.net.local.entity.UserTestRoom
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * Describe:
 * <p></p>
 *
 * @author zhouhuan
 * @Date 2020/12/1
 */
class MainHomeViewModel : BaseRefreshViewModel<Demo>() {
    private val dataRepositoryRepository: DataRepositorySource = DataRepository()

    private val recipesLiveDataPrivate = MutableLiveData<Resource<List<Demo>>>()
    val recipesLiveData: LiveData<Resource<List<Demo>>> get() = recipesLiveDataPrivate

    private val userTestRoomLiveDataPrivate = MutableLiveData<Resource<List<UserTestRoom>>>()
    val userTestRoomLiveData: LiveData<Resource<List<UserTestRoom>>> get() = userTestRoomLiveDataPrivate

    fun getRecipes() {
        viewModelScope.launch {
            postShowTransLoadingViewEvent(true)
            dataRepositoryRepository.requestRecipes().collect {
                recipesLiveDataPrivate.value = it
                postShowTransLoadingViewEvent(false)
                postStopRefreshEvent()
            }
        }
    }

    fun insertUserTestRoom(userTestRoom: UserTestRoom) {
        viewModelScope.launch {
            dataRepositoryRepository.insertUserTestRoom(userTestRoom).collect {
                showToastMessage("插入数据成功$it")
            }
        }
    }

    fun getUserTestRoom() {
        viewModelScope.launch {
            postShowTransLoadingViewEvent(true)
            dataRepositoryRepository.getAllUserTestRoom().collect {
                userTestRoomLiveDataPrivate.value = it
                postShowTransLoadingViewEvent(false)
            }
        }
    }

    fun showToastMessage(msg: String) {
        postShowToastViewEvent(msg)
    }

    override fun refreshData() {
        getRecipes()
    }

    override fun loadMore() {

    }

}
