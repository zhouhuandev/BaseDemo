package com.hzsoft.basedemo.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hzsoft.basedemo.MyApp
import com.hzsoft.lib.net.DataRepository
import com.hzsoft.lib.net.DataRepositorySource
import com.hzsoft.lib.net.dto.Demo
import com.hzsoft.lib.net.dto.Resource
import com.hzsoft.lib.net.local.LocalData
import com.hzsoft.lib.net.remote.RemoteData
import com.hzsoft.lib.net.remote.RetrofitManager
import com.hzsoft.lib.net.utils.Network
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * Describe:
 * <p></p>
 *
 * @author zhouhuan
 * @Date 2020/12/1
 */
class MainViewModel : ViewModel() {
    val dataRepositoryRepository: DataRepositorySource = DataRepository(
        RemoteData(RetrofitManager(), Network(MyApp.getContext())),
        LocalData(),
        Dispatchers.IO
    )
    val recipesLiveDataPrivate = MutableLiveData<Resource<List<Demo>>>()
    val recipesLiveData: LiveData<Resource<List<Demo>>> get() = recipesLiveDataPrivate

    fun getRecipes() {
        viewModelScope.launch {
            dataRepositoryRepository.requestRecipes().collect {
                recipesLiveDataPrivate.value = it
            }
        }
    }
}