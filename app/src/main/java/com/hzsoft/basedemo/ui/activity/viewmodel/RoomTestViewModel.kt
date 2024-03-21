package com.hzsoft.basedemo.ui.activity.viewmodel

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.hzsoft.lib.base.mvvm.viewmodel.BaseRefreshViewModel
import com.hzsoft.lib.net.BaseDataRepository
import com.hzsoft.lib.net.dto.Resource
import com.hzsoft.lib.net.local.entity.UserTestRoom
import com.hzsoft.lib.net.utils.ext.launch
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

    var showEmpty = ObservableField(false)

    private val dataRepositoryRepository: BaseDataRepository = BaseDataRepository()

    private val userTestRoomLiveDataPrivate = MutableLiveData<Resource<List<UserTestRoom>>>()
    val userTestRoomLiveData: LiveData<Resource<List<UserTestRoom>>> get() = userTestRoomLiveDataPrivate

    private val insertUserPrivate = MutableLiveData<Resource<UserTestRoom>>()
    val insertUser: LiveData<Resource<UserTestRoom>> get() = insertUserPrivate

    private val pageSize = 10
    private val currentPageNumber = MutableLiveData<Int>()

    private val mUserTestRoomLists = ArrayList<UserTestRoom>()
    val items: ArrayList<UserTestRoom>
        get() = mUserTestRoomLists

    private fun setCurrentPageNumber(pageNumber: Int) {
        currentPageNumber.value = pageNumber
    }

    fun insertUserTestRoom(userTestRoom: UserTestRoom) {
        viewModelScope.launch {
            dataRepositoryRepository.insertUserTestRoom(userTestRoom).collect {
                insertUserPrivate.postValue(Resource.Success(userTestRoom.apply {
                    // 更新 id
                    val data = it.data ?: 0
                    id = data
                }))
                showToastMessage("插入数据成功$it")
                showEmpty.set(false)
            }
        }
    }

    fun deleteUserTestRoom(userTestRoom: UserTestRoom) {
        viewModelScope.launch {
            dataRepositoryRepository.removeUserTestRoom(userTestRoom).collect {
                showToastMessage("删除数据成功$it")
                showEmpty.set(mUserTestRoomLists.isEmpty())
            }
        }
    }

    fun getUserTestRoom() {
        viewModelScope.launch {
            dataRepositoryRepository.getAllUserTestRoom().collect {
                userTestRoomLiveDataPrivate.value = it
                postStopRefreshEvent()
                postStopLoadMoreWithNoMoreDataEvent()
            }
        }
    }

    private fun getUserTestRoom(isRefresh: Boolean) {
        if (isRefresh) {
            setCurrentPageNumber(1)
        }
        val pageNumber = currentPageNumber.value ?: 1
        viewModelScope.launch {
            dataRepositoryRepository.getUserTestRoom(pageSize, pageNumber).collect {
                it.launch { r ->
                    setCurrentPageNumber(pageNumber + 1)
                    val nullOrEmpty = r.isNullOrEmpty()
                    if (isRefresh) {
                        showEmpty.set(nullOrEmpty)
                        postStopRefreshEvent(!nullOrEmpty)
                    } else {
                        if (nullOrEmpty) {
                            postStopLoadMoreWithNoMoreDataEvent()
                        } else {
                            postStopLoadMoreEvent()
                        }
                    }
                }
                userTestRoomLiveDataPrivate.postValue(it)
            }
        }
    }

    fun saveUpdateUserTestRoomData(isRefresh: Boolean, items: List<UserTestRoom>) {
        if (isRefresh) {
            mUserTestRoomLists.clear()
        }
        mUserTestRoomLists.addAll(items)
    }

    private fun showToastMessage(msg: String) {
        postShowToastViewEvent(msg)
    }

    override fun refreshData() {
        getUserTestRoom(true)
    }

    override fun loadMore() {
        getUserTestRoom(false)
    }
}