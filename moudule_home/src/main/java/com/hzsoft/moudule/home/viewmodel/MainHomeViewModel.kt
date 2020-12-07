package com.hzsoft.moudule.home.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.fly.tour.common.util.log.KLog
import com.hzsoft.lib.common.mvvm.viewmodel.BaseViewModel
import com.hzsoft.lib.common.utils.ext.view.showToast
import com.hzsoft.lib.domain.entity.Demo
import com.hzsoft.lib.net.DataRepository
import com.hzsoft.lib.net.DataRepositorySource
import com.hzsoft.lib.net.config.NetAppContext
import com.hzsoft.lib.net.dto.Resource
import com.hzsoft.lib.net.error.ApiException
import com.hzsoft.lib.net.error.mapper.ErrorManager
import com.hzsoft.lib.net.error.mapper.ErrorMapper
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
class MainHomeViewModel(application: Application) : BaseViewModel(application) {
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

}