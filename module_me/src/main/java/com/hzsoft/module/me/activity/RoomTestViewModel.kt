package com.hzsoft.module.me.activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.hzsoft.lib.base.mvvm.viewmodel.BaseRefreshViewModel
import com.hzsoft.lib.net.BaseDataRepository
import com.hzsoft.lib.net.dto.Resource
import com.hzsoft.lib.net.local.entity.UserTestRoom
import kotlinx.coroutines.launch

/**
 *
 *
 * @author zhouhuan
 * @time 2021/11/23
 */
class RoomTestViewModel(state: SavedStateHandle) :
    BaseRefreshViewModel() {

    private val savedStateHandle = state

    private val dataRepositoryRepository: BaseDataRepository = BaseDataRepository()

    private val userTestRoomLiveDataPrivate = MutableLiveData<Resource<List<UserTestRoom>>>()
    val userTestRoomLiveData: LiveData<Resource<List<UserTestRoom>>> get() = userTestRoomLiveDataPrivate

    fun insertUserTestRoom(userTestRoom: UserTestRoom) {
        viewModelScope.launch {
            dataRepositoryRepository.insertUserTestRoom(userTestRoom).collect {
                showToastMessage("插入数据成功$it")
            }
        }
    }

    fun deleteUserTestRoom(userTestRoom: UserTestRoom) {
        viewModelScope.launch {
            dataRepositoryRepository.removeUserTestRoom(userTestRoom).collect {
                showToastMessage("删除数据成功$it")
                refreshData()
            }
        }
    }

    private fun getUserTestRoom() {
        viewModelScope.launch {
            dataRepositoryRepository.getAllUserTestRoom().collect {
                userTestRoomLiveDataPrivate.value = it
                postStopRefreshEvent()
            }
        }
    }

    private fun showToastMessage(msg: String) {
        postShowToastViewEvent(msg)
    }

    override fun refreshData() {
        getUserTestRoom()
    }

    override fun loadMore() {

    }
}